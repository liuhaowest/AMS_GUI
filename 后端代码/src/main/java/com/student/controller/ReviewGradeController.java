package com.student.controller;

import com.alibaba.fastjson.JSONObject;
import com.student.common.Result;
import com.student.servie.ReviewService;
import com.student.vo.QueryBox;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/review")
public class ReviewGradeController {

    @Autowired
    private ReviewService reviewService;

    @PostMapping("/getReviewList")
    public Result getReviewList(@RequestBody QueryBox queryBox){
        return reviewService.getReviewList(queryBox.getCurrent(),queryBox.getStReviewGrade());
    }

    @GetMapping("/getReviewById")
    public Result getReviewById(int id){
        return reviewService.getReviewById(id);
    }

    @PostMapping("/createReviewByStu")
    public Result createReviewByStu(@RequestBody JSONObject jsonObject){
        return reviewService.createReviewByStu(jsonObject.getIntValue("classRoomId"), jsonObject.getString("stuDesc"),jsonObject.getIntValue("oldGrade"));
    }

    @PostMapping("/updateReviewByTea")
    public Result updateReviewByTea(MultipartFile[] files, String teaDesc, int id, int type,@RequestParam(required = false) int newGrade){
        return reviewService.updateReviewByTea(files, teaDesc, id, type,newGrade);
    }

    @PostMapping("/updateByAdmin")
    public Result updateByAdmin(@RequestBody JSONObject jsonObject){
        return reviewService.updateByAdmin(jsonObject.getString("adminDesc"),jsonObject.getIntValue("type"),jsonObject.getIntValue("id"));
    }

    @GetMapping("/getReviewByRoomId")
    public Result getReviewByRoomId(long roomId){
        return reviewService.getReviewByRoomId(roomId);
    }
}
