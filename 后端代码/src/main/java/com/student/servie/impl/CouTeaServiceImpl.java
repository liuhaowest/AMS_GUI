package com.student.servie.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.student.common.Result;
import com.student.dao.entity.*;
import com.student.dao.service.*;
import com.student.servie.CouStuService;
import com.student.servie.CouTeaService;
import com.student.vo.VoStudent;
import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

/**
 * @author 李123
 */
@Service
public class CouTeaServiceImpl implements CouTeaService {

    @Autowired
    private IStGradeStuService gradeStuService;
    @Autowired
    private IStUserService userService;
    @Autowired
    private IStReGradeStuService reGradeStuService;

    @Override
    public Result updateGrade(int grade,Long roomId,Long stuId) {
        //计算对应绩点,满分绩点为5
        double gpa=grade>=60?grade/10.0-5:0;
        StGradeStu gradeStu=new StGradeStu();
        gradeStu.setGrade(grade);
        gradeStu.setPoint(gpa);
        StReGradeStu reGradeStu = new StReGradeStu();
        reGradeStu.setGrade(grade);
        reGradeStu.setPoint(gpa);
        gradeStuService.update(gradeStu, new QueryWrapper<StGradeStu>().eq("stu_id",stuId)
                .eq("room_id",roomId));
        reGradeStuService.update(reGradeStu, new QueryWrapper<StReGradeStu>().eq("stu_id",stuId)
                .eq("room_id",roomId));
        return Result.succ(null);
    }

    @Override
    public Result exitRoom(Long roomId, Long stuId,int couType) {
        if(couType==1){
            if(reGradeStuService.remove(new QueryWrapper<StReGradeStu>().eq("stu_id",stuId)
                    .eq("room_id",roomId))){
                return Result.succ(null);
            }else {
                return Result.fail("退出课堂失败");
            }
        }else {
            if(gradeStuService.remove(new QueryWrapper<StGradeStu>().eq("stu_id",stuId)
                    .eq("room_id",roomId))){
                return Result.succ(null);
            }else {
                return Result.fail("退出课堂失败");
            }
        }
    }

    @Override
    public Result upGrade(MultipartFile multipartFile,Long roomId) throws IOException {
        // 创建一个临时文件
        if(multipartFile.getOriginalFilename()!=null){
            File file = File.createTempFile(multipartFile.getOriginalFilename(), null);
            ImportParams params = new ImportParams();
            // 将MultipartFile对象的内容写入临时文件中
            multipartFile.transferTo(file);
            List<VoStudent> list = ExcelImportUtil.importExcel(file, VoStudent.class,params);
            boolean delete = file.delete();
            return Result.succ(checkFile(list,roomId));
        }else {
            return Result.fail("文件上传失败");
        }
    }

    /**
     * 校验文件
     * @param list
     * @return
     */
    public Map<Integer,List<VoStudent>> checkFile(List<VoStudent> list, Long roomId){
        Map<Integer, List<VoStudent>> resultMap = new HashMap<>();
        Map<String, VoStudent> map = new HashMap<>();
        List<VoStudent> repeatList=new LinkedList<>();
        List<VoStudent> resultList=new LinkedList<>();
        List<VoStudent> illegalList=new LinkedList<>();
        for(VoStudent vo:list){
            if(!checkStudent(vo,roomId)){
                illegalList.add(vo);
                continue;
            }
            String key=vo.getUsername();
            if(!map.containsKey(key)){
                map.put(key,vo);
                resultList.add(vo);
            }else {
                repeatList.add(vo);
                repeatList.add(map.get(key));
                resultList.remove(vo);
            }
        }
        for (VoStudent vo : resultList) {
            //更新成绩
            StUser stUser = userService.getOne(new QueryWrapper<StUser>().eq("username", vo.getUsername()));
            updateGrade(vo.getGrade(),roomId, stUser.getId());
        }
        resultMap.put(0,illegalList);
        resultMap.put(1,repeatList);
        return resultMap;
    }

    /**
     * 判断学生是否选择该课程
     */
    public boolean checkStudent(VoStudent vo,Long roomId){
        long stuId=0;
        StUser stUser = userService.getOne(new QueryWrapper<StUser>().eq("username", vo.getUsername()));
        if(stUser!=null){
            stuId=stUser.getId();
        }else {
            return false;
        }
        Map<String,Long> map=new HashMap<>();
        map.put("stu_id",stuId);
        map.put("room_id",roomId);
        return gradeStuService.getOne(new QueryWrapper<StGradeStu>().allEq(map))!=null||reGradeStuService.getOne(new QueryWrapper<StReGradeStu>().allEq(map))!=null;
    }

}

