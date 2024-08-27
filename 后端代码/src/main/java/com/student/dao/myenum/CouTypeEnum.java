package com.student.dao.myenum;

import com.baomidou.mybatisplus.annotation.EnumValue;

public enum CouTypeEnum {

    General_Education(0, "通识教育"),
    Professional_Foundation(1, "专业基础课"),
    Professional_Core(2, "专业核心课"),
    Professional_Practice(3, "专业实践课");

    @EnumValue
    private final int code;

    private final String couType;


    CouTypeEnum(int code, String couType) {
        this.code = code;
        this.couType = couType;
    }

    public String getCouType() {
        return couType;
    }

    public int getCode() {
        return code;
    }

}
