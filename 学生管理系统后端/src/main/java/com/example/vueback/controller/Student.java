package com.example.vueback.controller;
import com.example.vueback.pojo.Ifo;
import com.example.vueback.pojo.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@CrossOrigin
public class Student {
    @Autowired
    private com.example.vueback.Service.Student family;
//  获取信息接口
    @GetMapping("/getinfo")
    public Result getinfo(){
        log.info("获取信息");
        return Result.success(family.getallinfo());
    }

//  新增信息接口
    @PostMapping("/addinfo")
    public Result addinfo(@RequestBody  Ifo ifo){
        log.info("新增信息");
        family.addinfo(ifo);
        return Result.success();
    }

    @GetMapping("/search")
    public Result searchinfo(Integer id){
        log.info("搜查信息");
        return Result.success(family.searchinfo(id));
    }
}
// 搜查信息接口


