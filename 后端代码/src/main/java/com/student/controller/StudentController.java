package com.student.controller;

import com.student.common.Result;
import com.student.servie.StudentService;
import com.student.vo.VoUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author 李123
 */
@RestController
@RequestMapping("/stu")
public class StudentController {

    @Autowired
    private StudentService studentService;

    @GetMapping("/getMajorClass")
    public Result getMajorClass(){
        return studentService.getMajorClass();
    }

    @GetMapping("/getDorm")
    public Result getDorm(){
        return studentService.getDorm();
    }

    @PostMapping("/updateStu")
    public Result updateStu(@RequestBody VoUser voUser){
        return studentService.updateStu(voUser);
    }

    @RequestMapping("/upAvatar")
    public Result upAvatar(@RequestBody MultipartFile file){
        return studentService.upAvatar(file);
    }
}
