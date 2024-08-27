package com.student.vo;

import lombok.Data;

import java.util.LinkedList;

/**
 * @author 李123
 * 用于匹配前端传来的数据，以及返回给前端的数据
 */
@Data
public class VoUser {
    private Long id;
    private String username;
    private String password;
    private String email;
    private String confirm;
    private String phoneNum;
    private String avatar;
    private String realName;
    private LinkedList<String> roles;
    /**
     * 映射前端的宿舍地址与地址号,0-index为id,1-index为地址,2-index为宿舍号
     */
    private Object [] dorm;

    private String sex;
    private String age;
    private String stuDay;
    private String idCard;
    private int grade;
    /**
     * 映射前端的专业班级 0-index为名称,1-index为id
     */
    private Object [] majorClass;
    /**
     * 映射前端的专业 0-index为名称,1-index为id
     */
    private Object [] major;
    /**
     * 注册时是否为教师 0为学生，1为教师
     */
    private int isTeacher;
}
