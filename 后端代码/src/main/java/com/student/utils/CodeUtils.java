package com.student.utils;



import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;

import java.util.HashMap;
import java.util.Map;

/**
 * @author 李123
 */
public class CodeUtils {
    //生成代码包结构
    public static void main(String[] args) {
        String property = System.getProperty("user.dir");
        FastAutoGenerator.create("jdbc:mysql://localhost:3306/springboot_student?useUnicode=true&characterEncoding=utf-8&useSSL=false&serverTimezone=UTC", "root", "password")
                .globalConfig(builder -> {
                    builder.author("liHeWei") // 设置作者
                            .enableSwagger() // 开启 swagger 模式
                            .outputDir(property+"/src/main/java"); // 指定输出目录
                })
                .packageConfig(builder -> {
                    Map<OutputFile,String> packageMap=new HashMap<>();
                    packageMap.put(OutputFile.xml, property+"/src/main/resources/mapper");
                    packageMap.put(OutputFile.mapper,property+"/src/main/java/com/student/dao/mapper");
                    packageMap.put(OutputFile.entity,property+"/src/main/java/com/student/dao/entity");
                    packageMap.put(OutputFile.service,property+"/src/main/java/com/student/dao/service");
                    packageMap.put(OutputFile.serviceImpl,property+"/src/main/java/com/student/dao/service/impl");
                    builder.parent("com") // 设置父包名
                            .moduleName("student.dao") // 设置父包模块名
                            .pathInfo(packageMap);

                })
                .strategyConfig(builder -> {
                    builder.addInclude("st_user","st_course","st_dorm","st_grade_stu",
                            "st_role","st_grade_tea","st_major","st_class","st_class_room",
                            "st_bing_course","st_review_grade","st_re_grade_stu","grade","stu_point","stu_grade","stu_avg_point");// 设置需要生成的表名
                })
                .strategyConfig(builder -> {
                    builder.entityBuilder()
                            .enableLombok(); //开启lombok
                } )
                .execute();
    }
}
