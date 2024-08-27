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
 * @since 2022-04-29
 */
@Getter
@Setter
@TableName("st_dorm")
@ApiModel(value = "StDorm对象", description = "")
public class StDorm implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("宿舍id")
    @TableId(type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("宿舍号")
    private String dormNum;

    @ApiModelProperty("宿舍地址号")
    private String dormAddress;


}
