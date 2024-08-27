package com.student.controller.common;

import com.student.common.Result;
import com.student.servie.common.CouService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 李123
 * 课程的公共控制层
 */
@RestController
@RequestMapping("/comm")
public class CouController {

    @Autowired
    private CouService couService;

    @GetMapping("/getRoomById")
    public Result getRoomById(Long id){
        return couService.getRoomById(id);
    }

    @GetMapping("/getStuList")
    public Result getStuList(Long roomId,int current){
        return couService.getStuList(roomId,current);
    }

}
