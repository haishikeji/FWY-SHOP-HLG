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
@ApiModel(value = "商品规格列表Vo", description = "商品规格列表Vo")
public class CommoditySpecificationListVo implements Serializable {

    @ApiModelProperty(value = "规格id")
    private String id;

    @ApiModelProperty(value = "商品id")
    private String commodityId;

    @ApiModelProperty(value = "店长价")
    private BigDecimal shopownerPrice;

    @ApiModelProperty(value = "售价")
    private BigDecimal sellPrice;

    @ApiModelProperty(value = "原价")
    private BigDecimal originalPrice;

    @ApiModelProperty(value = "商品单位")
    private String unit;

    @ApiModelProperty(value = "库存")
    private Integer stock;

    @ApiModelProperty(value = "规格名称")
    private String specificationName;

    @ApiModelProperty(value = "规格图片地址")
    private String specificationPicture;

    @ApiModelProperty(value = "商品重量（克）")
    private BigDecimal commodityWeight;

    @ApiModelProperty(value = "商品已售数量")
    private Integer soldQuantity;

    @ApiModelProperty(value = "商品未售数量")
    private Integer unsoldQuantity;

}