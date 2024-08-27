package com.student.controller;

import com.student.common.Result;
import com.student.servie.CouStuService;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 李123
 */
@RestController
@RequiresRoles(logical = Logical.OR, value = {"student", "admin"})
@RequestMapping("/stu/room")
public class StuRoomController {

    @Autowired
    private CouStuService couStuService;

    @GetMapping("/getRooms")
    public Result getRooms(int current,int id){
        return couStuService.getRoomList(current,id);
    }

    @GetMapping("/getRoomByKey")
    public Result getRoomByKey(String classKey,int id){
        return couStuService.getRoomByKey(classKey,id);
    }

    @GetMapping("/bindRoom")
    public Result bindRoom(Long roomId,int type){
        return couStuService.bingRoom(roomId,type);
    }

    @GetMapping("/menuInit")
    public Result menuInit(){
        return couStuService.menuInit();
    }

    @GetMapping("/getRoomsByYear")
    public Result getRoomsByYear(String stuYear,int couType){
        return couStuService.getRoomsByYear(stuYear,couType);
    }

    @GetMapping("/exitRoom")
    public Result exitRoom(Long roomId,int couType){
        return couStuService.exitRoom(roomId,couType);
    }
}
