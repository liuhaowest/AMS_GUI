package com.student.vo;

import lombok.Data;
import org.jeecgframework.poi.excel.annotation.Excel;

/**
 * 用于excel导入的工具类
 */
@Data
public class VoStudent {
    @Excel(name = "学号")
    private String username;
    @Excel(name = "姓名")
    private String realName;
    @Excel(name = "成绩")
    private int grade;
}
