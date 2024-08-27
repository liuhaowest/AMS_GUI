package com.student.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class VoReview {

    private String type;

    private String stuId;

    private String teaId;

    private Long roomId;

    private String state;

    private String finalState;

    private String desc;

    private String examPic;

    private String date;

    private Long oldGrade;

    private Long newGrade;
}
