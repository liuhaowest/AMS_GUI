package com.student.vo;

import lombok.Data;

/**
 * @author 李123
 * 前端课堂的映射类
 */
@Data
public class VoRoom {

    private Long id;
    private String classDesc;
    private String major;
    private String stuYear;
    private Long courseId;
    private String couName;
    private String classKey;
    private String bcImg;
    private String timing;
    private Long stuSum;
    private String couState;
    private double ratio;

}
