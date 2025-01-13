package com.px.business.manage.other.entity;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.px.common.api.base.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import java.time.LocalDateTime;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @@author 品讯科技
 * @Description: entity
 * @Date: 2021-03-24
 **/
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "Coupon", description = "优惠券")
@TableName("tb_coupon")
public class Coupon extends BaseEntity {

    @ApiModelProperty(value = "优惠券名称")
    @TableField("coupon_name")
    private String couponName;

    @ApiModelProperty(value = "封面图")
    @TableField("cover_url")
    private String coverUrl;

    @ApiModelProperty(value = "满金额")
    @TableField("full_amount")
    private BigDecimal fullAmount;

    @ApiModelProperty(value = "减金额")
    @TableField("reduce_amount")
    private BigDecimal reduceAmount;

    @ApiModelProperty(value = "优惠券使用类型id")
    @TableField("type_id")
    private String typeId;

    @ApiModelProperty(value = "数量")
    @TableField("coupon_number")
    private Integer couponNumber;

    @ApiModelProperty(value = "已领取数量")
    @TableField("coupon_received")
    private Integer couponReceived;

    @ApiModelProperty(value = "有效期")
    @TableField("term_validity")
    private LocalDateTime termValidity;

    @ApiModelProperty(value = "状态 0- 上架 1-下架")
    @TableField("state")
    private Integer state;

    @ApiModelProperty(value = "创建者id")
    @TableField("create_id")
    private String createId;

    @ApiModelProperty(value = "修改用户id")
    @TableField("update_id")
    private String updateId;


}