package com.student.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VoCourse {

    private Integer id;
    private String couName;
    private double couGrade;
    private Integer couTime;
    private String major;
    private double ratio;
    private String couType;
    private String couAttribute;
}
