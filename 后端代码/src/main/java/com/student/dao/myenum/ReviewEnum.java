//package com.student.dao.myenum;
//
//import com.baomidou.mybatisplus.annotation.EnumValue;
//
//public enum ReviewEnum {
//
//    CREATE_BY_STU("0","学生申请完毕"),
//    UPDATE_BY_TEA_TRUE("1","教师审核通过"),
//    UPDATE_BY_TEA_FALSE("2","教师审核未通过"),
//    UPDATE_BY_ADMIN_TRUE("3","管理员审核通过"),
//    UPDATE_BY_ADMIN_FALSE("4","管理员审核未通过");
//
//    @EnumValue
//    private final String code;
//    private final String state;
//
//    ReviewEnum(String code,String state){
//        this.state=state;
//        this.code=code;
//    }
//
//    public String getCode() {
//        return code;
//    }
//
//    public String getState() {
//        return state;
//    }
//}
