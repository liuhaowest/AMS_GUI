package com.student.controller;

import cn.hutool.core.util.RandomUtil;
import com.alibaba.fastjson.JSONObject;
import com.student.common.Result;
import com.student.servie.EmailService;
import com.student.servie.LoginService;
import com.student.vo.VoUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.InvocationTargetException;

/**
 * <p>
 *  user前端控制器
 * </p>
 *
 * @author liHeWei
 * @since 2022-04-29
 */
@RestController
public class StUserController {
    @Autowired
    private LoginService loginService;
    @Autowired
    private EmailService emailService;

    //登录
    @RequestMapping("/login")
    public Result login(@RequestBody JSONObject jsonObject){
        return loginService.login(jsonObject.getString("account"), jsonObject.getString("password"));
    }


    //注册用户
    @RequestMapping("/register")
    public Result register(@RequestBody VoUser voUser) {
        return loginService.register(voUser);
    }

    //获取用户信息
    @RequestMapping("/getInfo")
    public Result getInfo(){
        return loginService.getInfo();
    }

    //发送邮件
    @GetMapping("/sendEmail")
    public Result sendEmail(String email){
        //生成五位随机验证码，注入邮件，同时返回给前端由于验证
        String confirm= RandomUtil.randomString(5);
        //预防邮件发送失败
        System.out.println(confirm);
        emailService.sendEmail(confirm,email);
        return Result.succ(confirm);
    }


}
