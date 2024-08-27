package com.student.servie;

import cn.hutool.core.lang.UUID;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.student.common.Result;
import com.student.dao.entity.StGradeStu;
import com.student.dao.entity.StGradeTea;
import com.student.dao.entity.StReviewGrade;
import com.student.dao.entity.StUser;
import com.student.dao.service.IStGradeStuService;
import com.student.dao.service.IStGradeTeaService;
import com.student.dao.service.IStReviewGradeService;
import com.student.dao.service.IStUserService;
import com.student.vo.VoReview;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class ReviewService {

    @Autowired
    private IStReviewGradeService reviewGradeService;
    @Autowired
    private IStGradeTeaService gradeTeaService;

    @Autowired
    private IStGradeStuService gradeStuService;
    @Autowired
    private IStUserService userService;

    //统一页条数
    private static final int pageSize=10;

    //统一日期格式
    private static final String dataFormat="yyyy-MM-dd HH:mm:ss";

    private static Map<String,String> reviewMap;

    static {
        reviewMap=new HashMap<>();
        reviewMap.put("0","学生申请完毕");
        reviewMap.put("1","教师审核通过");
        reviewMap.put("2","教师审核未通过");
        reviewMap.put("3","管理员审核通过");
        reviewMap.put("4","管理员审核未通过");
    }

    @Value("${gradeImgFile}")
    private String gradeImgUrl;

    /**
     * 获取复核信息
     * @param current 当前页
     * @param reviewGrade 对象
     * @return s/f
     */
    public Result getReviewList(int current, StReviewGrade reviewGrade){
        StUser user = (StUser) SecurityUtils.getSubject().getPrincipal();
        IPage<StReviewGrade> page=new Page<>(current,pageSize);
        if(reviewGrade!=null){
            //是否为管理员
            if(user.getRoleId()!=3){
                reviewGrade.setTeaId(user.getUsername());
            }
            return Result.succ(reviewGradeService.page(page,new QueryWrapper<>(reviewGrade)));
        }else {
            if(user.getRoleId()!=3){
                return Result.succ(reviewGradeService.page(page,new QueryWrapper<StReviewGrade>().eq("tea_id",user.getUsername())));
            }else {
                return Result.succ(reviewGradeService.page(page));
            }
        }
    }

    /**
     * 根据id获取复核信息
     * @param id id
     * @return s/f
     */
    public Result getReviewById(int id) {
        List<VoReview> resultList = new LinkedList<>();
        StReviewGrade stReviewGrade = reviewGradeService.getById(id);
        if (stReviewGrade.getStuDate() != null) {
            VoReview voReview = new VoReview();
            updateReview(voReview,stReviewGrade);
            voReview.setType("student");
            voReview.setState(stReviewGrade.getState().split(",")[0]);
            voReview.setDesc(stReviewGrade.getStuDesc());
            voReview.setDate(stReviewGrade.getStuDate());
            resultList.add(voReview);
        }
        if (stReviewGrade.getTeaDate() != null) {
            VoReview voReview = new VoReview();
            updateReview(voReview,stReviewGrade);
            voReview.setType("teacher");
            voReview.setState(stReviewGrade.getState().split(",")[1]);
            voReview.setDesc(stReviewGrade.getTeaDesc());
            voReview.setExamPic(stReviewGrade.getExamPic());
            voReview.setDate(stReviewGrade.getTeaDate());
            resultList.add(voReview);
        }
        if (stReviewGrade.getAdminDate() != null) {
            VoReview voReview = new VoReview();
            updateReview(voReview,stReviewGrade);
            voReview.setType("admin");
            voReview.setState(stReviewGrade.getState().split(",")[2]);
            voReview.setDesc(stReviewGrade.getAdminDesc());
            voReview.setDate(stReviewGrade.getAdminDate());
            resultList.add(voReview);
        }
        return Result.succ(resultList);
    }

    public void updateReview(VoReview voReview,StReviewGrade stReviewGrade){
        String[] split = stReviewGrade.getState().split(",");
        voReview.setFinalState(split[split.length-1]);
        voReview.setOldGrade(stReviewGrade.getOldGrade());
        voReview.setNewGrade(stReviewGrade.getNewGrade());
        voReview.setStuId(stReviewGrade.getStuId());
        voReview.setTeaId(stReviewGrade.getTeaId());
        voReview.setRoomId(stReviewGrade.getRoomId());
    }

    /**
     * 查找学生复核信息
     * @param roomId 课堂id
     * @return s/f
     */
    public Result getReviewByRoomId(long roomId){
        StUser user = (StUser) SecurityUtils.getSubject().getPrincipal();
        return Result.succ(reviewGradeService.getOne(new QueryWrapper<StReviewGrade>().eq("stu_id",user.getUsername()).eq("room_id",roomId)));
    }

    /**
     * 学生申请成绩复核
     * @param classRoomId id
     * @param stuDesc 学生备注
     * @return s/f
     */
    public Result createReviewByStu(long classRoomId,String stuDesc,long oldGrade){
        StReviewGrade reviewGrade=new StReviewGrade();
        StUser user = (StUser) SecurityUtils.getSubject().getPrincipal();
        List<StGradeTea> stGradeTeas = gradeTeaService.list(new QueryWrapper<StGradeTea>().eq("class_room_id", classRoomId));
        String teaId=userService.getById(stGradeTeas.get(0).getTeaId()).getUsername();
        SimpleDateFormat format=new SimpleDateFormat(dataFormat);
        reviewGrade.setStuDate(format.format(new Date()));
        reviewGrade.setRoomId(classRoomId);
        reviewGrade.setTeaId(teaId);
        reviewGrade.setStuId(user.getUsername());
        reviewGrade.setStuDesc(stuDesc);
        reviewGrade.setOldGrade(oldGrade);
        reviewGrade.setState(reviewMap.get("0")+",");
        reviewGradeService.save(reviewGrade);
        return Result.succ(reviewGrade.getId());
    }

    /**
     *
     * @param files 文件列表
     * @param teaDesc 教师备注
     * @param id reviewId
     * @param type 0:通过,1:未通过
     * @return s/f
     */
    public Result updateReviewByTea(MultipartFile[] files,String teaDesc,int id,int type,long newGrade){
        StringBuilder examPic = new StringBuilder();
        List<File> uploadedFiles = new ArrayList<File>();
        for(MultipartFile file :files){
            String oriFilename= file.getOriginalFilename();
            //用uuid构造唯一文件名
            String newFilename= UUID.randomUUID()+"-"+id+"-"+oriFilename;
            //文件路径
            File filePath = new File(gradeImgUrl);
            if(!filePath.exists()){
                if(!filePath.mkdirs()) {
                    return Result.fail("文件路径创建失败");
                }
            }
            //上传文件
            try {
                File resFile = new File(gradeImgUrl + newFilename);
                uploadedFiles.add(resFile);
                file.transferTo(resFile);
            } catch (IOException e) {
                for(File temFile:uploadedFiles){
                    temFile.delete();
                }
                return Result.succ("试卷图片上传失败");
            }
            examPic.append(newFilename).append(",");
        }
        String examUrl = examPic.substring(0, examPic.length() - 1);
        StReviewGrade reviewGrade=new StReviewGrade();
        reviewGrade.setId(id);
        if(type==0){
            reviewGrade.setState(getState(id)+reviewMap.get("1")+",");
        }else {
            reviewGrade.setState(getState(id)+reviewMap.get("2")+",");
        }
        SimpleDateFormat format=new SimpleDateFormat(dataFormat);
        reviewGrade.setNewGrade(newGrade);
        reviewGrade.setTeaDate(format.format(new Date()));
        reviewGrade.setTeaDesc(teaDesc);
        reviewGrade.setExamPic(examUrl);
        reviewGrade.setOldGrade(reviewGradeService.getById(id).getOldGrade());
        reviewGradeService.updateById(reviewGrade);
        return Result.succ(examUrl);
    }

    /**
     * 管理员审核
     * @param adminDesc 管理员备注
     * @param type 0:通过,1:未通过
     * @return s/f
     */
    public Result updateByAdmin(String adminDesc,int type,int id){
        StReviewGrade reviewGrade = new StReviewGrade();
        StReviewGrade oldReviewGrade = reviewGradeService.getById(id);
        if(type==0){
            reviewGrade.setState(getState(id)+reviewMap.get("3")+",");
            //更新成绩
            StUser stUser = userService.getOne(new QueryWrapper<StUser>().eq("username", oldReviewGrade.getStuId()));
            StGradeStu gradeStu = new StGradeStu();
            gradeStu.setGrade(oldReviewGrade.getNewGrade().intValue());
            gradeStu.setPoint(oldReviewGrade.getNewGrade()>=60?oldReviewGrade.getNewGrade()/10.0-5:0);
            gradeStuService.update(gradeStu,new QueryWrapper<StGradeStu>().eq("stu_id",stUser.getId()).eq("room_id",oldReviewGrade.getRoomId()));
        }else {
            reviewGrade.setState(getState(id)+reviewMap.get("4")+",");
        }
        reviewGrade.setId(id);
        reviewGrade.setOldGrade(oldReviewGrade.getOldGrade());
        reviewGrade.setNewGrade(oldReviewGrade.getNewGrade());
        reviewGrade.setAdminDesc(adminDesc);
        SimpleDateFormat format=new SimpleDateFormat(dataFormat);
        reviewGrade.setAdminDate(format.format(new Date()));
        reviewGradeService.updateById(reviewGrade);
        return Result.succ(null);
    }

    public String getState(int id){
        return reviewGradeService.getById(id).getState();
    }
}
