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
@TableName("stu_avg_point")
@ApiModel(value = "StuAvgPoint对象", description = "VIEW")
public class StuAvgPoint implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("学生id")
    private Long stuId;

    @ApiModelProperty("学号")
    private String username;

    @ApiModelProperty("姓名")
    private String realName;

    @ApiModelProperty("专业名称")
    private String majorName;

    private Double avgGpa;

    private Long ranking;


}
