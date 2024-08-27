package com.student.controller;

import com.student.common.Result;
import com.student.servie.ScoreAnalysisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/scoreAnalysis")
public class ScoreAnalysisController {

    @Autowired
    private ScoreAnalysisService scoreAnalysisService;

    @GetMapping("/getRankAndGpa")
    public Result getRankAndGpa(){
        return scoreAnalysisService.getRankAndGpa();
    }

    @GetMapping("/getRankAndGpaByYear")
    public Result getRankAndGpaByYear(){
        return scoreAnalysisService.getRankAndGpaByYear();
    }

    @GetMapping("/getGradeInterval")
    public Result getGradeInterval(){
        return scoreAnalysisService.getGradeInterval();
    }

    @GetMapping("/getScoreAnalysis")
    public Result getScoreAnalysis(){
        return scoreAnalysisService.getScoreAnalysis();
    }

    @GetMapping("/getCourseByType")
    public Result getCourseByType(int type){
        return scoreAnalysisService.getCourseByType(type);
    }
}
