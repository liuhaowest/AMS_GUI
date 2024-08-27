package com.student.dao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <p>
 *
 * </p>
 *
 * @author liHeWei
 * @since 2022-05-03
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("st_grade_tea")
@ApiModel(value = "StGradeTea对象", description = "")
public class StGradeTea implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("教师id")
    private Long teaId;

    @ApiModelProperty("课程id")
    private Long couId;

    @ApiModelProperty("课堂id")
    private Long classRoomId;



}
