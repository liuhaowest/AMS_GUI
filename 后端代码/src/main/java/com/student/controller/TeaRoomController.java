package com.student.controller;

import com.alibaba.fastjson.JSONObject;
import com.student.common.Result;
import com.student.servie.CouTeaService;
import com.student.servie.CourseService;
import com.student.vo.VoRoom;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.ParseException;


/**
 * @author 李123
 */
@RestController
@RequestMapping("/cou")
public class TeaRoomController {

    @Autowired
    private CourseService courseService;
    @Autowired
    private CouTeaService couTeaService;

    @GetMapping("/labelInit")
    public Result getLabelInit(){
        return courseService.labelInit();
    }

    @GetMapping("/getTeaRoom")
    public Result getTeaRoom(String stuYear,String major){
        return courseService.getTeaRoom(stuYear,major);
    }

    @GetMapping("/getCourse")
    public Result getCourse(String major){
        return courseService.getCourse(major);
    }

    @GetMapping("/getMajor")
    public Result getMajor(){
        return courseService.getMajor();
    }

    @PostMapping("/bindCourse")
    public Result bindCourse(@RequestBody JSONObject couIds){
        return courseService.bindCourse(couIds.getJSONArray("couIds"),couIds.getString("major"));
    }

    @PostMapping("/upBcImg")
    public Result upBcImg(MultipartFile file,Long roomId){
        return courseService.upBcImg(file,roomId);
    }

    @PostMapping("/createRoom")
    public Result createRoom(@RequestBody VoRoom room){
        return courseService.createRoom(room);
    }

    @PostMapping("/changeState")
    public Result changeState(@RequestBody JSONObject jsonObject){
        return courseService.changeState(jsonObject.getIntValue("state"), jsonObject.getLong("roomId"));
    }

    @PostMapping("/timingStart")
    public Result timingStart(@RequestBody JSONObject jsonObject) throws ParseException {
        return courseService.timingStart(jsonObject.getString("time"), jsonObject.getLong("roomId"));
    }

    @PostMapping("/updateGrade")
    public Result updateGrade(@RequestBody JSONObject jsonObject){
        return couTeaService.updateGrade(jsonObject.getIntValue("grade"),
                jsonObject.getLong("roomId"),
                jsonObject.getLong("stuId"));
    }

    @PostMapping("/exitRoomByTea")
    public Result exitRoom(@RequestBody JSONObject jsonObject){
        return couTeaService.exitRoom(jsonObject.getLong("roomId"),jsonObject.getLong("stuId"),jsonObject.getIntValue("couType"));
    }

    @PostMapping("/upGrade")
    public Result upGrade(MultipartFile file,Long roomId) throws IOException {
        return couTeaService.upGrade(file,roomId);
    }

}
