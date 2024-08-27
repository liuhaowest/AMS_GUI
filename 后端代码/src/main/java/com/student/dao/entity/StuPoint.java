package com.student.dao.entity;

import com.baomidou.mybatisplus.annotation.TableName;
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
@TableName("stu_point")
@ApiModel(value = "StuPoint对象", description = "VIEW")
public class StuPoint implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("学生id")
    private Long stuId;

    @ApiModelProperty("所属学年例（2020,1）")
    private String stuYear;

    @ApiModelProperty("学号")
    private String username;

    @ApiModelProperty("姓名")
    private String realName;

    @ApiModelProperty("专业名称")
    private String majorName;


    private Double avgGpa;

    private Long ranking;


}
