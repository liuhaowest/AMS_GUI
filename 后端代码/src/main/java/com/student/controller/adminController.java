package com.student.controller;

import com.student.common.Result;
import com.student.dao.entity.StBingCourse;
import com.student.dao.entity.StDorm;
import com.student.dao.entity.StUser;
import com.student.servie.AdminService;
import com.student.vo.QueryBox;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
public class adminController {

    @Autowired
    private AdminService adminService;

    @PostMapping("/getUserList")
    public Result getUserList(@RequestBody QueryBox queryBox){
        return adminService.getUserList(queryBox.getCurrent(),queryBox.getUserType(),queryBox.getStUser());
    }

    @PostMapping("/getDormList")
    public Result getDormList(@RequestBody QueryBox queryBox){
        return adminService.getDormList(queryBox.getCurrent(),queryBox.getStDorm());
    }

    @PostMapping("/saveOrUpdateDorm")
    public Result saveOrUpdateDorm(@RequestBody StDorm stDorm){
        return adminService.updateDorm(stDorm);
    }

    @PostMapping("/saveOrUpdateTea")
    public Result saveOrUpdateTea(@RequestBody StUser stUser){
        return adminService.updateTea(stUser);
    }

    @PostMapping("delTea")
    public Result delTea(@RequestBody StUser stUser){
        return adminService.delTea(stUser.getId());
    }

    @PostMapping("delStu")
    public Result delStu(@RequestBody StUser stUser){
        return adminService.delStu(stUser.getId());
    }

    @PostMapping("delDorm")
    public Result delDorm(@RequestBody StDorm stDorm){
        return adminService.delDorm(stDorm.getId());
    }

    @PostMapping("addBingCourse")
    public Result addBingCourse(@RequestBody StBingCourse stBingCourse){
        return adminService.addBingCourse(stBingCourse);
    }

    @PostMapping("delBingCourse")
    public Result delBingCourse(@RequestBody StBingCourse stBingCourse){
        return adminService.delBingCourse(stBingCourse);
    }

    @GetMapping("getBingCourseList")
    public Result getBingCourseList(){
        return adminService.getBingCourseList();
    }

}
