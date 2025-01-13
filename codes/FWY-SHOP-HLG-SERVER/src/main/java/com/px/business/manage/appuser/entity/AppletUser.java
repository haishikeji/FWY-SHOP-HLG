package com.px.business.manage.appuser.entity;

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
 * @Date: 2021-02-24
 **/
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "AppletUser", description = "小程序用户表")
@TableName("tb_applet_user")
public class AppletUser extends BaseEntity {

    @ApiModelProperty(value = "用户昵称")
    @TableField("nickname")
    private String nickname;

    @ApiModelProperty(value = "用户手机号")
    @TableField("phone")
    private String phone;

    @ApiModelProperty(value = "用户头像")
    @TableField("head_url")
    private String headUrl;

    @ApiModelProperty(value = "微信uid")
    @TableField("openid")
    private String openid;

    @ApiModelProperty(value = "性别 0女 1男")
    @TableField("sex")
    private Integer sex;

    @ApiModelProperty(value = "状态 0：正常 1：禁用")
    @TableField("state")
    private Integer state;

    @ApiModelProperty(value = "创建者ID")
    @TableField("create_id")
    private String createId;

    @ApiModelProperty(value = "创建者ID")
    @TableField("update_id")
    private String updateId;

    @ApiModelProperty(value = "用户积分")
    @TableField("points")
    private Integer points;

    @ApiModelProperty(value = "商家id")
    @TableField("shop_id")
    private String shopId;

    @ApiModelProperty(value = "是否店长")
    @TableField("is_shopowner")
    private Boolean isShopowner;

}