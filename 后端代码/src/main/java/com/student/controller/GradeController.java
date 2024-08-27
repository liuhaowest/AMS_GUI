package com.student.controller;

import com.student.common.Result;
import com.student.servie.GradeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/grade")
public class GradeController {

    @Autowired
    private GradeService gradeService;

    @GetMapping("/getPoint")
    public Result getPoint(String stuYear){
        return Result.succ(gradeService.getPoint(stuYear));
    }

    @GetMapping("/getPointInStuYear")
    public Result getPointInStuYear(){
        return gradeService.getPointInStuYear();
    }

    @GetMapping("/getAvgPointLimit")
    public Result getAvgPointLimit(String majorName){
        return gradeService.getAvgPointLimit(majorName);
    }

    @GetMapping("/getGrade")
    public Result getGrade(String stuYear){
        return gradeService.getGrade(stuYear);
    }

    @GetMapping("/getSumGrade")
    public Result getSumGrade(){
        return gradeService.getSumGrade();
    }

    @GetMapping("/getGradePoint")
    public Result getGradePoint(int couId,String stuYear){
        return gradeService.getGradePoint(couId, stuYear);
    }

    @GetMapping("/initLineChartPoint")
    public Result initLineChartPoint(int type){
        return gradeService.initLineChartPoint(type);
    }
}
