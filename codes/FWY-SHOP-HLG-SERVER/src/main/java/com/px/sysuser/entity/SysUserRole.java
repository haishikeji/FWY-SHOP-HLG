package com.px.sysuser.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @@author 品讯科技
 * @Description: entity
 * @Date: 2020-05-26
 **/
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "SysUserRole", description = "系统用户角色关系表")
@TableName("tb_sys_user_role")
public class SysUserRole implements Serializable {

    /**
     * id 雪花算法生成
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    @ApiModelProperty(value = "用户ID")
    @TableField("uid")
    private String uid;

    @ApiModelProperty(value = "角色ID")
    @TableField("role_id")
    private String roleId;


}