package com.student.controller;

import com.student.common.Result;
import com.student.utils.SqlUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @Autowired
    private SqlUtils sqlUtils;

    @GetMapping("/test")
    public Result test(){
        sqlUtils.test4();
        return Result.succ(null);
    }
}
