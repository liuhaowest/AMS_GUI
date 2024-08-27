package com.student.dao.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 *
 * </p>
 *
 * @author liHeWei
 * @since 2022-05-03
 */
@Data
@TableName("st_grade_stu")
@ApiModel(value = "StGradeStu对象", description = "")
public class StGradeStu implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("用户id")
    private Long stuId;

    @ApiModelProperty("课堂id")
    private Long roomId;

    @ApiModelProperty("成绩")
    private int grade;

    @ApiModelProperty("绩点")
    private double point;

    @ApiModelProperty("课程id")
    private Long couId;


}
