package com.student.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author 李123
 */
@Configuration
public class MvcConfig implements WebMvcConfigurer {

    //用户头像图片读取路径
    @Value("${stuImgFile}")
    private String imgUrl;
    //课程背景图片回显路径
    @Value("${couImgFile}")
    private String couImgUrl;

    @Value("${gradeImgFile}")
    private String gradeImgFile;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/uploads/images/user/**").addResourceLocations("file:"+imgUrl);
        registry.addResourceHandler("/uploads/images/cou/**").addResourceLocations("file:"+couImgUrl);
        registry.addResourceHandler("/uploads/images/grade/**").addResourceLocations("file:"+gradeImgFile);
    }


}
