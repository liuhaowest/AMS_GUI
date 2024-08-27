package com.student.controller;

import com.student.common.Result;
import com.student.dao.entity.StCourse;
import com.student.dao.entity.StMajor;
import com.student.servie.MajorCourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/major")
public class MajorCourseController {
    @Autowired
    private MajorCourseService majorCourseService;

    @GetMapping("/getCourseByMajor")
    public Result getCourseByCourse(String majorName){
        return majorCourseService.getCourseByMajor(majorName);
    }

    @PostMapping("/addMajor")
    public Result addMajor(@RequestBody StMajor major){
        return majorCourseService.addMajor(major.getName());
    }

    @PostMapping("/delMajor")
    public Result delMajor(@RequestBody StMajor major){
        return majorCourseService.delMajor(major.getName());
    }

    @GetMapping("/getCourseById")
    public Result getCourseById(int couId){
        return majorCourseService.getCourseById(couId);
    }

    @PostMapping("/saveOrUpdateCourse")
    public Result saveOrUpDateCourse(@RequestBody StCourse stCourse){
        return majorCourseService.addCourse(stCourse);
    }

    @PostMapping("/delCourse")
    public Result delCourse(@RequestBody StCourse stCourse){
        return majorCourseService.delCourse(stCourse.getId());
    }
}
