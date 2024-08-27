package com.student.servie.common;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.student.common.Result;
import com.student.dao.entity.*;
import com.student.dao.mapper.StCourseMapper;
import com.student.dao.mapper.StGradeStuMapper;
import com.student.dao.service.*;
import com.student.vo.VoRoom;
import com.student.vo.VoUser;
import io.swagger.models.auth.In;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @author 李123
 * 课程的公共业务类
 */
@Service
public class CouService {

    @Autowired
    private IStClassRoomService classRoomService;
    @Autowired
    private IStMajorService majorService;
    @Autowired
    private IStGradeStuService gradeStuService;
    @Autowired
    private StCourseMapper courseMapper;
    @Autowired
    private IStReGradeStuService reGradeStuService;
    @Autowired
    private IStUserService userService;

    /**
     * 根据课堂id查询信息
     */
    public Result getRoomById(Long id){
        StClassRoom classRoom = classRoomService.getById(id);
        VoRoom room=new VoRoom();
        BeanUtils.copyProperties(classRoom,room);
        //查询专业名称
        StMajor major = majorService.getById(classRoom.getMajorId());
        room.setMajor(major.getName());
        //查询选课人数
        Long stuSum=gradeStuService.count(new QueryWrapper<StGradeStu>().eq("room_id",id))+reGradeStuService.count(new QueryWrapper<StReGradeStu>().eq("room_id",id));
        room.setStuSum(stuSum);
        StCourse course=courseMapper.getCourse(id);
        //查询课程名称
        room.setCouName(course.getCouName());
        //赋予课程状态
        room.setCouState(classRoom.getState().getState());
        //查询平时分占比
        room.setRatio(course.getRatio());
        return Result.succ(room);
    }

    /**
     * 分页获取选择当前课堂的学生信息
     * @param roomId 课堂id
     * @return list
     */
    public Result getStuList(Long roomId,int current){
        List<Long> ids=new LinkedList<>();
        Map<String,Object> resMap=new HashMap<>();
        Map<Long,Integer> gradeMap=new HashMap<>();
        List<VoUser> list=new LinkedList<>();
        gradeStuService.list(new QueryWrapper<StGradeStu>().eq("room_id",roomId)).forEach(item->{
            ids.add(item.getStuId());
            gradeMap.put(item.getStuId(), item.getGrade());
        });
        reGradeStuService.list(new QueryWrapper<StReGradeStu>().eq("room_id",roomId)).forEach(item->{
            ids.add(item.getStuId());
            gradeMap.put(item.getStuId(), item.getGrade());
        });
        Page<StUser> page = new Page<>(current,20);
        QueryWrapper<StUser> query = new QueryWrapper<>();
        if(!ids.isEmpty()){
            query.in("id", ids);
        }else {
            resMap.put("records",list);
            return Result.succ(resMap);
        }
        Page<StUser> resPage=userService.page(page,query);
        resPage.getRecords().forEach(item->{
            VoUser user=new VoUser();
            BeanUtils.copyProperties(item,user);
            if(!gradeMap.isEmpty()){
                user.setGrade(gradeMap.get(item.getId()));
            }
            list.add(user);
        });
        resMap.put("records", list);
        resMap.put("total",resPage.getTotal());
        resMap.put("size",resPage.getSize());
        resMap.put("current",resPage.getCurrent());
        return Result.succ(resMap);
    }
}
