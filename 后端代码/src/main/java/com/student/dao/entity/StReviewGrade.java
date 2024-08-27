package com.student.dao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;

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
 * @since 2024-02-22
 */
@Getter
@Setter
@TableName("st_review_grade")
@ApiModel(value = "StReviewGrade对象", description = "")
public class StReviewGrade implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("学生学号")
    private String stuId;

    @ApiModelProperty("教师编号")
    private String teaId;

    @ApiModelProperty("课堂id")
    private Long roomId;

    @ApiModelProperty("(0:学生申请，1：教师审核完毕，2：管理员审核完毕)")
    private String state;

    @ApiModelProperty("学生意见")
    private String stuDesc;

    @ApiModelProperty("教师意见")
    private String teaDesc;

    @ApiModelProperty("管理员意见")
    private String adminDesc;

    @ApiModelProperty("试卷图片，名称以','隔开")
    private String examPic;

    @ApiModelProperty("创建时间")
    private String stuDate;

    @ApiModelProperty("教师审批时间")
    private String teaDate;

    @ApiModelProperty("管理员审批时间")
    private String adminDate;

    @ApiModelProperty("旧成绩")
    private Long oldGrade;

    @ApiModelProperty("新成绩")
    private Long newGrade;


}
