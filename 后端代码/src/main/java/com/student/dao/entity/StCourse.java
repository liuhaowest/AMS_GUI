package com.student.dao.entity;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;

import com.student.dao.myenum.CouTypeEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 *
 * </p>
 *
 * @author liHeWei
 * @since 2022-04-29
 */
@Getter
@Setter
@TableName("st_course")
@ApiModel(value = "StCourse对象", description = "")
public class StCourse implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("课程id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("课程名称")
    private String couName;

    @ApiModelProperty("学分")
    private double couGrade;

    @ApiModelProperty("学时")
    private Integer couTime;

    @ApiModelProperty("所属专业")
    private String major;

    @ApiModelProperty("考试分占中成绩的比值(剩余为平时分)")
    private double ratio;

    @ApiModelProperty("课程类别（0:通识教育，1：专业基础课，2：专业核心课，3：专业实践课）")
    private CouTypeEnum couType;

    @ApiModelProperty("课程属性")
    private String couAttribute;



}
