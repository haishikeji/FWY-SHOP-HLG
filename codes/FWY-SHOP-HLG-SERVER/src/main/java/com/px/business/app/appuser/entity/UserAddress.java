package com.px.business.app.appuser.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.px.common.api.base.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @@author 品讯科技
 * @Description: entity
 * @Date: 2021-02-25
 **/
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "UserAddress", description = "收货地址表")
@TableName("tb_user_address")
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class UserAddress extends BaseEntity {

    @ApiModelProperty(value = "用户id")
    @TableField("user_id")
    private String userId;

    @ApiModelProperty(value = "收件人")
    @TableField("addressee")
    private String addressee;

    @ApiModelProperty(value = "门牌号")
    @TableField("house_number")
    private String houseNumber;

    @ApiModelProperty(value = "标签")
    @TableField("address_label")
    private String addressLabel;

    @ApiModelProperty(value = "收件地址")
    @TableField("address")
    private String address;

    @ApiModelProperty(value = "默认地址标志 0-默认 1-不默认")
    @TableField("default_sign")
    private Integer defaultSign;

    @ApiModelProperty(value = "联系电话")
    @TableField("contact_number")
    private String contactNumber;

    @ApiModelProperty(value = "创建者id")
    @TableField("create_id")
    private String createId;

    @ApiModelProperty(value = "修改用户id")
    @TableField("update_id")
    private String updateId;

    @ApiModelProperty(value = "经度")
    @TableField("longitude")
    private Double longitude;

    @ApiModelProperty(value = "纬度")
    @TableField("latitude")
    private Double latitude;
}