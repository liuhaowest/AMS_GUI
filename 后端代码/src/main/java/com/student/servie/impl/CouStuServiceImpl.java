package com.student.servie.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.student.common.Result;
import com.student.dao.entity.*;
import com.student.dao.mapper.StCourseMapper;
import com.student.dao.service.*;
import com.student.servie.CouStuService;
import com.student.utils.MainUtils;
import com.student.vo.VoRoom;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * 学生-课堂业务实现类
 * @author 李123
 */
@Service
public class CouStuServiceImpl implements CouStuService {

    @Autowired
    private IStClassRoomService classRoomService;
    @Autowired
    private IStGradeStuService gradeStuService;
    @Autowired
    private IStGradeTeaService gradeTeaService;
    @Autowired
    private StCourseMapper courseMapper;
    @Autowired
    private IStReGradeStuService reGradeStuService;

    @Autowired
    private IStMajorService majorService;
    @Autowired
    private IStBingCourseService bingCourseService;

    @Override
    public Result getRoomList(int current,int id) {
        StUser user= (StUser) SecurityUtils.getSubject().getPrincipal();
        Map<String,Object> resultMap=new HashMap<>(4);
        Page<StClassRoom> page=new Page<>(current,30);
        List<VoRoom> list=new LinkedList<>();
        //查询当前选课任务
        StBingCourse bingCourse = bingCourseService.getById(id);
        if(bingCourse==null){
            return Result.fail("查询不到选课任务，请联系管理员");
        }
        //校验选课时间
        if(isBetweenBingCourse(bingCourse.getStartTime(), bingCourse.getEndTime(), LocalDateTime.now())){
            return Result.fail("未在选课时间内");
        }
        //查询学生已绑定课堂的id集
        List<Long> ids=new LinkedList<>();
        gradeStuService.list(new QueryWrapper<StGradeStu>().eq("stu_id", user.getId())).forEach(item->{
            ids.add(item.getRoomId());
        });
        reGradeStuService.list(new QueryWrapper<StReGradeStu>().eq("stu_id", user.getId())).forEach(item->{
            ids.add(item.getRoomId());
        });
        Page<StClassRoom> resPage;
        QueryWrapper<StClassRoom> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("stu_year",bingCourse.getStuYear());
        if (!ids.isEmpty()) {
            queryWrapper.notIn("id", ids);
        }
        resPage = classRoomService.page(page,queryWrapper);
        //类转换
        resPage.getRecords().forEach(item->{
            list.add(convert(item));
        });
        resultMap.put("records", list);
        resultMap.put("total",resPage.getTotal());
        resultMap.put("size",resPage.getSize());
        resultMap.put("current",resPage.getCurrent());
        return Result.succ(resultMap);
    }
    @Override
    public boolean isBetweenBingCourse(String start,String end,LocalDateTime now){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"); // 定义时间格式
        LocalDateTime startTime = LocalDateTime.parse(start, formatter);
        LocalDateTime endTime = LocalDateTime.parse(end, formatter);
        return now.isAfter(endTime) || now.isBefore(startTime);
    }

    @Override
    public Result getRoomByKey(String classKey,int id) {
        //查询当前选课任务
        StBingCourse bingCourse = bingCourseService.getById(id);
        if(bingCourse==null){
            return Result.fail("查询不到选课任务，请联系管理员");
        }
        StClassRoom classRoom=classRoomService.getOne(new QueryWrapper<StClassRoom>().eq("class_key",classKey));

//        StClassRoom classRoom=classRoomService.getOne(new QueryWrapper<StClassRoom>().eq("class_key",classKey).eq("stu_year",bingCourse.getStuYear()));
        return Result.succ(convert(classRoom));
    }

    @Override
    public Result bingRoom(Long roomId,int type) {
        StUser user= (StUser) SecurityUtils.getSubject().getPrincipal();
        if(majorService.getById(user.getMajorId())==null){
            return Result.fail("请完善个人专业信息");
        }
        if(!checkBingRoom(roomId)){
            return Result.fail("已绑定过相同课程");
        }
        if(type==0){
            StGradeStu gradeStu=new StGradeStu();
            gradeStu.setRoomId(roomId);
            gradeStu.setStuId(user.getId());
            Long couId = gradeTeaService.getOne(new QueryWrapper<StGradeTea>().eq("class_room_id", roomId)).getCouId();
            gradeStu.setCouId(couId);
            if(gradeStuService.save(gradeStu)){
                return Result.succ(null);
            }else {
                return Result.fail("绑定失败");
            }
        }else {
            StReGradeStu reGradeStu=new StReGradeStu();
            reGradeStu.setStuId(user.getId());
            reGradeStu.setRoomId(roomId);
            Long couId = gradeTeaService.getOne(new QueryWrapper<StGradeTea>().eq("class_room_id", roomId)).getCouId();
            reGradeStu.setCouId(couId);
            if(reGradeStuService.save(reGradeStu)){
                return Result.succ(null);
            }else {
                return Result.fail("绑定失败");
            }
        }
    }

    /**
     * 校验学生不能绑定同一课程
     * @param roomId 课堂id
     * @return s/f
     */
    public boolean checkBingRoom(Long roomId){
        StUser user= (StUser) SecurityUtils.getSubject().getPrincipal();
        StClassRoom classRoom = classRoomService.getById(roomId);
        String couName = courseMapper.getCourse(classRoom.getId()).getCouName();
        List<StGradeStu> list = gradeStuService.list(new QueryWrapper<StGradeStu>().eq("stu_id", user.getId()));
        List<StReGradeStu> reList = reGradeStuService.list(new QueryWrapper<StReGradeStu>().eq("stu_id", user.getId()));
        for(StGradeStu item:list){
            StClassRoom itemClassRoom = classRoomService.getById(item.getRoomId());
            String itemCouName = courseMapper.getCourse(itemClassRoom.getId()).getCouName();
            if(couName.equals(itemCouName)){
                return false;
            }
        }
        for(StReGradeStu item:reList){
            StClassRoom itemClassRoom = classRoomService.getById(item.getRoomId());
            String itemCouName = courseMapper.getCourse(itemClassRoom.getId()).getCouName();
            if(couName.equals(itemCouName)){
                return false;
            }
        }
        return true;
    }

    @Override
    public Result menuInit() {
        //获取入学学期
        return Result.succ(MainUtils.initStuYear());
    }

    @Override
    public Result getRoomsByYear(String year,int couType) {
        List<VoRoom> rooms=new LinkedList<>();
        StUser user = (StUser) SecurityUtils.getSubject().getPrincipal();
        //获取学生已选课堂的id集
        List<Long> ids=new LinkedList<>();
        if(couType==0){
            gradeStuService.list(new QueryWrapper<StGradeStu>().eq("stu_id", user.getId())).forEach(item->{
                ids.add(item.getRoomId());
            });
        }else {
            reGradeStuService.list(new QueryWrapper<StReGradeStu>().eq("stu_id", user.getId())).forEach(item->{
                ids.add(item.getRoomId());
            });
        }
        if (!ids.isEmpty()) {
            classRoomService.list(new QueryWrapper<StClassRoom>().in("id", ids)
                    .eq("stu_year", year)).forEach(item -> {
                rooms.add(convert(item));
            });
        }
        return Result.succ(rooms);
    }

    @Override
    public Result exitRoom(Long roomId,int couType) {
        StUser user=(StUser) SecurityUtils.getSubject().getPrincipal();
        StClassRoom classRoom = classRoomService.getById(roomId);
        List<StBingCourse> list = bingCourseService.list(new QueryWrapper<StBingCourse>().eq("stu_year", classRoom.getStuYear()).eq("type", couType));
        if(list.size()==0){
            return Result.fail("未在选课时间内");
        } else if (list.size()==1) {
            StBingCourse bingCourse = list.get(0);
            if(isBetweenBingCourse(bingCourse.getStartTime(), bingCourse.getEndTime(), LocalDateTime.now())){
                return Result.fail("未在选课时间内");
            }
        } else {
            return Result.fail("系统错误，请联系管理员");
        }
        if(couType==1){
            if(reGradeStuService.remove(new QueryWrapper<StReGradeStu>().eq("stu_id",user.getId())
                    .eq("room_id",roomId))){
                return Result.succ(null);
            }else {
                return Result.fail("退出课堂失败");
            }
        }else {
            if(gradeStuService.remove(new QueryWrapper<StGradeStu>().eq("stu_id",user.getId())
                    .eq("room_id",roomId))){
                return Result.succ(null);
            }else {
                return Result.fail("退出课堂失败");
            }
        }
    }

    /**
     * vo类准换
     */
    private VoRoom convert(StClassRoom classRoom){
        VoRoom room=new VoRoom();
        BeanUtils.copyProperties(classRoom,room);
        if("".equals(classRoom.getTiming())){
            room.setTiming("待定");
        }
        //查询选课人数
        Long stuSum=gradeStuService.count(new QueryWrapper<StGradeStu>().eq("room_id",classRoom.getId()))+reGradeStuService.count(new QueryWrapper<StReGradeStu>().eq("room_id",classRoom.getId()));
        room.setStuSum(stuSum);
        //查询课程名称
        room.setCouName(courseMapper.getCourse(classRoom.getId()).getCouName());
        //赋予课程状态
        room.setCouState(classRoom.getState().getState());
        return room;
    }
}
