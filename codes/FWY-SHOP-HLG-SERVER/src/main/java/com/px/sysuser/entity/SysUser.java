package com.px.sysuser.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.px.common.api.base.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @@author 品讯科技
 * @Description: entity
 * @Date: 2020-05-26
 **/
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "SysUser", description = "系统用户表")
@TableName("tb_sys_user")
public class SysUser extends BaseEntity {

    @ApiModelProperty(value = "用户名")
    @TableField("nickname")
    private String nickname;

    @ApiModelProperty(value = "邮箱")
    @TableField("email")
    private String email;

    @ApiModelProperty(value = "手机")
    @TableField("phone")
    private String phone;

    @ApiModelProperty(value = "状态 0：正常 1：禁用")
    @TableField("state")
    private Integer state;

    @ApiModelProperty(value = "创建者ID")
    @TableField("create_id")
    private String createId;

    @ApiModelProperty(value = "登陆名")
    @TableField("login_name")
    private String loginName;

    @ApiModelProperty(value = "密码")
    @TableField("password")
    private String password;

    @ApiModelProperty(value = "平台id/商家id")
    @TableField("about_id")
    private String aboutId;

    @ApiModelProperty(value = "用户类型 0- 平台 1-商户")
    @TableField("user_type")
    private Integer userType;

    @ApiModelProperty(value = "性别 0女 1男")
    @TableField("sex")
    private Integer sex;

    @ApiModelProperty(value = "0- 商家负责人 1-商家员工")
    @TableField("type")
    private Integer type;


}