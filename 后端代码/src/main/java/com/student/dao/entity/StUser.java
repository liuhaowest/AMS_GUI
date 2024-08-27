package com.student.dao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.jeecgframework.poi.excel.annotation.Excel;

/**
 * @author liHeWei
 * @since 2022-04-29
 */
@Data
@TableName("st_user")
@ApiModel(value = "StUser对象", description = "")
public class StUser implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("学号")
    private String username;

    @ApiModelProperty("姓名")
    private String realName;

    @ApiModelProperty("密码")
    private String password;

    @ApiModelProperty("盐")
    private String salt;

    @ApiModelProperty("邮箱")
    private String email;

    @ApiModelProperty("手机号")
    private String phoneNum;

    @ApiModelProperty("角色Id")
    private Long roleId;

    @ApiModelProperty("宿舍Id")
    private Long  dormId;

    @ApiModelProperty("头像")
    private String avatar;

    @ApiModelProperty("性别")
    private String sex;

    @ApiModelProperty("年龄")
    private String age;

    @ApiModelProperty("入学日期")
    private String stuDay;

    @ApiModelProperty("身份证号")
    private String idCard;

    @ApiModelProperty("专业班级id")
    private Long majorClassId;

    @ApiModelProperty("所在专业id")
    private Long majorId;



}
