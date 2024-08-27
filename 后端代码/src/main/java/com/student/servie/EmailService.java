package com.student.servie;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.annotation.Resource;
import javax.mail.internet.MimeMessage;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * 处理邮件发送，效验
 * @author liHeWei
 * @since 2022-04-16
 */
@Service
public class EmailService {
    @Value("${spring.mail.username}")
    private String sendEmailName;
    @Resource
    private JavaMailSender javaMailSender;
    @Resource
    private TemplateEngine templateEngine;
    @Resource
    private RedisTemplate<String, Object> redisTemplate ;

    //发送邮件
    public void sendEmail(String confirm,String email) {
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);
            //设置邮件基本信息
            mimeMessageHelper.setSubject("欢迎来到李同学的学生成绩管理系统：个人账号注册");
            mimeMessageHelper.setFrom(sendEmailName);
            mimeMessageHelper.setTo(email);
            mimeMessageHelper.setSentDate(new Date());
            //设置邮件内容模板
            Context context = new Context();
            context.setVariable("confirm", confirm);
            //使用thymeleaf的模板引擎
            String text = templateEngine.process("email.html", context);
            mimeMessageHelper.setText(text, true);
            //发送邮件
            javaMailSender.send(mimeMessage);
            //将验证码写入redis并设置1分钟有效期
            redisTemplate.opsForValue().set(email, confirm, 1, TimeUnit.MINUTES);
        } catch (Exception  e) {
            e.printStackTrace();
        }
    }

}
