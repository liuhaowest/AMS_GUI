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
 * @since 2024-02-20
 */
@Getter
@Setter
@TableName("st_bing_course")
@ApiModel(value = "StBingCourse对象", description = "")
public class StBingCourse implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("开始时间")
    private String startTime;

    @ApiModelProperty("结束时间")
    private String endTime;

    @ApiModelProperty("所属学年例（2020,1）")
    private String stuYear;

    @ApiModelProperty("面向群体")
    private String target;

    @ApiModelProperty("建议")
    private String suggest;

    @ApiModelProperty("类型（0：普通，1：重修）")
    private int type;



}
