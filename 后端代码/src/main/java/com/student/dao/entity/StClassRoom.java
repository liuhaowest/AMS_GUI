package com.student.dao.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;

import com.student.dao.myenum.couEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 *
 * </p>
 *
 * @author liHeWei
 * @since 2022-05-12
 */
@Data
@TableName("st_class_room")
@ApiModel(value = "StClassRoom对象", description = "")
public class StClassRoom implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("班级描述")
    private String classDesc;

    @ApiModelProperty("课堂码")
    private String classKey;

    @ApiModelProperty("所属专业")
    private Long majorId;

    @ApiModelProperty("所属学年例（2020,1）")
    private String stuYear;

    @ApiModelProperty("开课时间(可定时)")
    private String timing;

    @ApiModelProperty("背景图片")
    private String bcImg;

    @ApiModelProperty("开课状态（0:未开始,1:进行中,2：已结束）")
    private couEnum state;


}
