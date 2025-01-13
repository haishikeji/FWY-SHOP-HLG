package com.px.business.manage.commodity.vo;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @@author 品讯科技
 * @Description: vo
 * @Date: 2021-03-01
 **/
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "CommoditySpecificationVo", description = "商品规格Vo")
public class CommoditySpecificationVo implements Serializable {

    @ApiModelProperty(value = "规格id")
    private String id;

    @ApiModelProperty(value = "条形码")
    private String barCode;

    @ApiModelProperty(value = "商品id")
    private String commodityId;

    @ApiModelProperty(value = "默认规格 0- 默认 1-其他")
    private Integer type;

    @ApiModelProperty(value = "售价")
    private BigDecimal sellPrice;

    @ApiModelProperty(value = "店长价")
    private BigDecimal shopownerPrice;


    @ApiModelProperty(value = "原价")
    private BigDecimal originalPrice;

    @ApiModelProperty(value = "商品单位")
    private String unit;

    @ApiModelProperty(value = "成本价")
    private BigDecimal costPrice;

    @ApiModelProperty(value = "库存")
    private Integer stock;

    @ApiModelProperty(value = "规格名称")
    private String specificationName;

    @ApiModelProperty(value = "规格图片地址")
    private String specificationPicture;

    @ApiModelProperty(value = "手续费")
    private BigDecimal serviceCharge;

    @ApiModelProperty(value = "商品重量（克）")
    private BigDecimal commodityWeight;

    @ApiModelProperty(value = "商品已售数量")
    private Integer soldQuantity;

    @ApiModelProperty(value = "商品未售数量")
    private Integer unsoldQuantity;

    @ApiModelProperty(value = "拼团价")
    private BigDecimal groupWorkPrice;
}