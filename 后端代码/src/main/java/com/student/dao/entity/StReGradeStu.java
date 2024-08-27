package com.student.dao.entity;

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
@TableName("st_re_grade_stu")
@ApiModel(value = "StReGradeStu对象", description = "")
public class StReGradeStu implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("学生id")
    private Long stuId;

    @ApiModelProperty("课堂id")
    private Long roomId;

    @ApiModelProperty("成绩")
    private Integer grade;

    @ApiModelProperty("绩点")
    private Double point;

    @ApiModelProperty("课程id")
    private Long couId;

}
