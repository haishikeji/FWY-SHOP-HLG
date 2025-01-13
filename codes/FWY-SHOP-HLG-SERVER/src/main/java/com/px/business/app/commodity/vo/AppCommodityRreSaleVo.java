package com.px.business.app.commodity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @@author 品讯科技
 * @Description: vo
 * @Date: 2021-03-01
 **/
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "AppCommodityRreSaleVo", description = "预售商品")
public class AppCommodityRreSaleVo implements Serializable {

    @ApiModelProperty(value = "id")
    private String id;

    @ApiModelProperty(value = "商品名称")
    private String commodityName;

    @ApiModelProperty(value = "商品封面图")
    private String coverUrl;

    @ApiModelProperty(value = "店长价")
    private BigDecimal shopownerPrice;

    @ApiModelProperty(value = "售价")
    private BigDecimal sellPrice;

    @ApiModelProperty(value = "原价")
    private BigDecimal originalPrice;

    @ApiModelProperty(value = "商品总数(限量)")
    private Integer stock;

    @ApiModelProperty(value = "剩余总数")
    private Integer unsoldQuantity;

    @ApiModelProperty(value = "开始时间")
    private LocalDateTime startTime;

    @ApiModelProperty(value = "结束时间(计算倒计时)")
    private LocalDateTime endTime;

    @ApiModelProperty(value = "店铺类型")
    private Integer shopType;

}