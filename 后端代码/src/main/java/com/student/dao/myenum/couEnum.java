package com.student.dao.myenum;
import com.baomidou.mybatisplus.annotation.EnumValue;

/**
 * 开课状态的枚举类
 * @author 李123
 */
public enum couEnum {
    /**
     * 各枚举
     */
    WAIT_START(0,"待开课"),
    DING(1,"进行中"),
    END(2,"已结束");

    private final String state;

    @EnumValue
    private final int code;

    couEnum(int code,String state){
        this.state=state;
        this.code=code;
    }

    public int getCode() {
        return code;
    }

    public String getState() {
        return state;
    }


}
