package com.student.servie;

import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.RandomUtil;
import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.student.common.Result;
import com.student.dao.entity.*;
import com.student.dao.mapper.StCourseMapper;
import com.student.dao.mapper.StGradeTeaMapper;
import com.student.dao.service.*;
import com.student.redis.delay.bean.Job;
import com.student.utils.MainUtils;
import com.student.vo.VoRoom;
import com.student.dao.myenum.couEnum;
import com.student.vo.VoStudent;
import org.apache.shiro.SecurityUtils;
import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.xml.crypto.Data;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 处理课程选修等相关信息
 */
@Service
public class CourseService {

    //课程背景图片上传地址
    @Value("${couImgFile}")
    private String couImgPath;

    @Autowired
    private IStMajorService majorService;
    @Autowired
    private IStCourseService courseService;
    @Autowired
    private StCourseMapper courseMapper;
    @Autowired
    private StGradeTeaMapper gradeTeaMapper;
    @Autowired
    private IStGradeTeaService gradeTeaService;
    @Autowired
    private IStGradeStuService gradeStuService;
    @Autowired
    private IStReGradeStuService reGradeStuService;
    @Autowired
    private IStClassRoomService classRoomService;
    @Autowired
    private JobService jobService;

    /**
     * 初始化教师学年栏
     */
    public Result labelInit(){
        StUser user = (StUser) SecurityUtils.getSubject().getPrincipal();
        List<String> resultList=new LinkedList<>();
        //构造查询条件
        QueryWrapper<StClassRoom> query=new QueryWrapper<>();
        List<Long> idList = getIdList(user.getId(), 1);
        int year;
        String time;
        //当前学期
        Calendar date = Calendar.getInstance();
        Map<String,Object> map= MainUtils.getStuDay(date.get(Calendar.YEAR),date.get(Calendar.MONTH));
        int temYear=(int)map.get("year");
        String temTime=(String)map.get("time");
        //最早学期
        if(idList.size()>0){
            query.in("id", idList)
                    .orderByAsc("stu_year");
            String firstYear=classRoomService.list(query).get(0).getStuYear();
            String[] split = firstYear.split(",");
            year=Integer.parseInt(split[0]);
            time=split[1];

        }else{
            year=temYear;
            time=temTime;
        }
//        year=2019;
//        time="一";
        for(int i=0;year<=temYear;i++,year++){
            if(year==temYear&&time.equals(temTime)){
                resultList.add(year+"-"+(year+1)+"第一学期");
                if("二".equals(temTime)){
                    resultList.add(year+"-"+(year+1)+"第"+temTime+"学期");
                }
                break;
            }
            if(i==0){
                if("一".equals(time)){
                    resultList.add(year+"-"+(year+1)+"第一学期");
                }
            }else {
                resultList.add(year+"-"+(year+1)+"第一学期");
            }
            resultList.add(year+"-"+(year+1)+"第二学期");
        }
        return Result.succ(resultList);
    }

    /**
     * 定时开课
     */
    public Result timingStart(String time,Long roomId) throws ParseException {
        SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date=format.parse(time);
        StClassRoom classRoom = new StClassRoom();
        classRoom.setId(roomId);
        classRoom.setTiming(time);
        //创建定时任务
        Job job=new Job();
        job.setDelayTime(date.getTime()-System.currentTimeMillis());
        job.setId(roomId);
        job.setTopic("test");
        jobService.addDefJob(job);
        if(classRoomService.updateById(classRoom)){
            return Result.succ(null);
        }else {
            return Result.fail("定时失败");
        }
    }

    /**
     * 更改课程状态
     */
    public Result changeState(int state,Long roomId){
        StClassRoom classRoom = new StClassRoom();
        SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        classRoom.setId(roomId);
        if(state==2){
            //结束课程
            classRoom.setState(couEnum.END);
        }else {
            //即刻开课
            classRoom.setState(couEnum.DING);
            classRoom.setTiming(format.format(new Date()));
        }
        if(classRoomService.updateById(classRoom)){
            return Result.succ(null);
        }else {
            return Result.fail("更改失败");
        }
    }

    /**
     * 根据学年和专业获取教师以创课堂
     */
    public Result getTeaRoom(String stuYear,String major){
        StUser user = (StUser) SecurityUtils.getSubject().getPrincipal();
        Long majorId=majorService.getOne(new QueryWrapper<StMajor>().eq("name",major)).getId();
        List<Long> idList = getIdList(user.getId(), 1);
        List<VoRoom> rooms=new ArrayList<>();
        if(idList.size()>0){
            //构造查询条件
            QueryWrapper<StClassRoom> query=new QueryWrapper<>();
            query.in("id",idList)
                    .eq("stu_year", stuYear)
                    .eq("major_id",majorId);
            List<StClassRoom> list = classRoomService.list(query);
            if(list.size()>0){
                list.forEach(item->{
                    VoRoom room=new VoRoom();
                    BeanUtils.copyProperties(item,room);
                    if("".equals(item.getTiming())){
                        room.setTiming("待定");
                    }
                    //查询选课人数
                    Long stuSum=gradeStuService.count(new QueryWrapper<StGradeStu>().eq("room_id",item.getId()));
                    Long reStuSum=reGradeStuService.count(new QueryWrapper<StReGradeStu>().eq("room_id",item.getId()));
                    //查询课程名称
                    room.setCouName(courseMapper.getCourse(item.getId()).getCouName());
                    room.setStuSum(stuSum+reStuSum);
                    //赋予课程状态
                    room.setCouState(item.getState().getState());
                    rooms.add(room);
                });
            }
        }
        return Result.succ(rooms);
    }

    /**
     * 更改课程背景图片
     */
    public Result upBcImg(MultipartFile file,Long roomId){
        StUser user= (StUser) SecurityUtils.getSubject().getPrincipal();
        String oriFilename= file.getOriginalFilename();
        //用uuid构造唯一文件名
        String newFilename= UUID.randomUUID()+"-"+user.getUsername()+"-"+oriFilename;
        //文件路径
        File filePath = new File(couImgPath);
        if(!filePath.exists()){
            if(!filePath.mkdirs()) {
                return Result.fail("文件路径创建失败");
            }
        }
        //删除旧文件
        String oldFilename=classRoomService.getById(roomId).getBcImg();
        if(!"couBcImg.jpg".equals(oldFilename)){
            File oldFile=new File(couImgPath+oldFilename);
            if(!oldFile.exists()||!oldFile.delete()){
                return Result.fail("原有图片移除失败");
            }
        }
        //上传文件
        try {
            file.transferTo(new File(couImgPath+newFilename));
        } catch (IOException e) {
            return Result.succ("背景图片上传失败");
        }
        //更新数据库
        StClassRoom classRoom=new StClassRoom();
        classRoom.setBcImg(newFilename);
        classRoom.setId(roomId);
        if(classRoomService.updateById(classRoom)){
            return Result.succ(newFilename);
        }else {
            return Result.fail("图片上传失败");
        }
    }

    /**
     * 获取专业信息
     */
    public Result getMajor(){
        return Result.succ(majorService.list());
    }

    /**
     * 根据专业获取课程
     */
    public Result getCourse(String major){
        //获取当前登录用户
        StUser user = (StUser) SecurityUtils.getSubject().getPrincipal();
        Map<String,Object> map=new HashMap<>(2);
        List<Long> idList=getIdList(user.getId(), 0);
        if(!idList.isEmpty()){
            //查询已选课程
            QueryWrapper<StCourse> alQuery = getQuery(major, idList, 1);
            map.put("alList",courseService.list(alQuery));
            //查询未选择的课程
            QueryWrapper<StCourse> noQuery = getQuery(major, idList, 0);
            map.put("noList",courseService.list(noQuery));
        }else {
            map.put("noList",courseService.list(new QueryWrapper<StCourse>().eq("major",major)));
            map.put("alList",new LinkedList<StCourse>());
        }
        return Result.succ(map);
    }

    /**
     * 教师创建课堂
     */
    public Result createRoom(VoRoom room){
        StUser user = (StUser) SecurityUtils.getSubject().getPrincipal();
        StClassRoom classRoom=new StClassRoom();
        classRoom.setClassDesc(room.getClassDesc());
        String year= room.getStuYear().substring(0,4);
        String time=room.getStuYear().substring(10, 11);
        classRoom.setStuYear(year+','+time);
        classRoom.setMajorId(majorService.getOne(new QueryWrapper<StMajor>().eq("name",room.getMajor())).getId());
        //生成课程码
        String classKey= RandomUtil.randomString(5).toUpperCase();
        classRoom.setClassKey(classKey);
        if(classRoomService.save(classRoom)){
            StGradeTea gradeTea=new StGradeTea();
            gradeTea.setClassRoomId(classRoom.getId());
            if(gradeTeaService.save(new StGradeTea(null, user.getId(), room.getCourseId(), classRoom.getId()))){
                return Result.succ(null);
            }else {
                return Result.fail("课堂创建失败");
            }
        }else {
            return Result.fail("课堂创建失败");
        }

    }

    /**
     * 教师绑定课程
     */
    public Result bindCourse(JSONArray couIds,String major){
        StUser user = (StUser) SecurityUtils.getSubject().getPrincipal();
        ArrayList<Integer> oldArr=new ArrayList<>();
        ArrayList<Integer> oldArrCopy=new ArrayList<>();
        //查询教师已选的该专业的课程id
        List<StGradeTea> stuList =gradeTeaMapper.getCourseByMajor(user.getId(), major);
        stuList.forEach(item->{
            oldArr.add(item.getCouId().intValue());
            oldArrCopy.add(item.getCouId().intValue());
        });
        //获取删除id集
        oldArr.removeAll(couIds);
        //获取新增id集
        couIds.removeAll(oldArrCopy);
        //删除
        oldArr.forEach(item->gradeTeaService.remove(new QueryWrapper<StGradeTea>()
        .eq("tea_id",user.getId())
        .eq("cou_id",item)
        ));
        //新增
        couIds.forEach(item->{
            StGradeTea gradeTea=new StGradeTea();
            gradeTea.setTeaId(user.getId());
            gradeTea.setCouId(Long.valueOf(String.valueOf(item)));
            gradeTeaService.save(gradeTea);
        });
        return Result.succ(null);
    }

    /**
     * 查询教师已选课程
     * @param teaId 教师id
     * @param isRoom 1:查询课堂 0:查询课程
     * @return list
     */
    private List<Long> getIdList(Long teaId,int isRoom){
        List<Long> idList=new LinkedList<>();
        QueryWrapper<StGradeTea> query=new QueryWrapper<>();
        query.eq("tea_id", teaId);
        if(isRoom==1){
            query.ne("class_room_id",0);
            List<StGradeTea> stuList = gradeTeaService.list(query);
            stuList.forEach(item->idList.add(item.getClassRoomId()));
        }else {
            query.eq("class_room_id",0);
            List<StGradeTea> stuList = gradeTeaService.list(query);
            stuList.forEach(item->idList.add(item.getCouId()));
        }
        return idList;
    }
    /**
     * 构造查询条件
     * @param isAl 是否已选修
     * @param major 专业
     * @param list 已选的id集
     */
    private  QueryWrapper<StCourse> getQuery(String major, List<Long> list, int isAl){
        QueryWrapper<StCourse> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("major",major);
        if(isAl==0){
            queryWrapper.notIn("id", list);
        }else {
            queryWrapper.in("id", list);
        }
        return queryWrapper;
    }

}
