package com.student.common;


import lombok.Data;

import java.io.Serializable;
/*
@author 李123
* 统一响应体
* */
@Data
public class Result implements Serializable {

    private int code;
    private String msg;
    private Object data;

    public static Result succ(Object data) {
        Result r = new Result();
        r.setCode(200);
        r.setMsg("成功");
        r.setData(data);
        return r;
    }

    public static Result fail(String msg) {
        return fail(400, msg, null);
    }

    public static Result fail(int code, String msg, Object data) {
        Result r = new Result();
        r.setCode(code);
        r.setMsg(msg);
        r.setData(data);
        return r;
    }

}
