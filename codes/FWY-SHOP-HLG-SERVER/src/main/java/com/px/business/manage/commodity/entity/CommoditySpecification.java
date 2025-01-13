package com.px.business.manage.commodity.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.px.common.api.base.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 * @@author 品讯科技
 * @Description: entity
 * @Date: 2021-03-01
 **/
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "CommoditySpecification", description = "商品规格表")
@TableName("tb_commodity_specification")
public class CommoditySpecification extends BaseEntity {

    @ApiModelProperty(value = "条形码")
    private String barCode;

    @ApiModelProperty(value = "商品id")
    @TableField("commodity_id")
    private String commodityId;

    @ApiModelProperty(value = "默认规格 0- 默认 1-其他")
    @TableField("type")
    private Integer type;

    @ApiModelProperty(value = "售价")
    @TableField("sell_price")
    private BigDecimal sellPrice;

    @ApiModelProperty(value = "店长价")
    @TableField("shopowner_price")
    private BigDecimal shopownerPrice;

    @ApiModelProperty(value = "原价")
    @TableField("original_price")
    private BigDecimal originalPrice;

    @ApiModelProperty(value = "拼团价")
    @TableField("group_work_price")
    private BigDecimal groupWorkPrice;

    @ApiModelProperty(value = "商品单位")
    @TableField("unit")
    private String unit;

    @ApiModelProperty(value = "成本价")
    @TableField("cost_price")
    private BigDecimal costPrice;

    @ApiModelProperty(value = "库存")
    @TableField("stock")
    private Integer stock;

    @ApiModelProperty(value = "规格名称")
    @TableField("specification_name")
    private String specificationName;

    @ApiModelProperty(value = "规格图片地址")
    @TableField("specification_picture")
    private String specificationPicture;

    @ApiModelProperty(value = "商品重量（克）")
    @TableField("commodity_weight")
    private BigDecimal commodityWeight;

    @ApiModelProperty(value = "商品已售数量")
    @TableField("sold_quantity")
    private Integer soldQuantity;

    @ApiModelProperty(value = "商品未售数量")
    @TableField("unsold_quantity")
    private Integer unsoldQuantity;

}