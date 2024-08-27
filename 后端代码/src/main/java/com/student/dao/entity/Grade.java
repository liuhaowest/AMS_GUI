package com.student.dao.entity;

import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * VIEW
 * </p>
 *
 * @author liHeWei
 * @since 2024-03-08
 */
@Getter
@Setter
@ApiModel(value = "Grade对象", description = "VIEW")
public class Grade implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("学生id")
    private Long stuId;

    @ApiModelProperty("姓名")
    private String realName;

    @ApiModelProperty("学号")
    private String username;

    @ApiModelProperty("学分")
    private Double couGrade;

    private Integer grade;

    private Double point;

    @ApiModelProperty("专业名称")
    private String majorName;

    @ApiModelProperty("课程名称")
    private String couName;

    @ApiModelProperty("所属专业")
    private Long majorId;

    @ApiModelProperty("所属学年例（2020,1）")
    private String stuYear;

    @ApiModelProperty("课程id")
    private Long couId;

    @ApiModelProperty("课程类型")
    private Long couType;

    private Long ranking;


}
