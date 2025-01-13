package com.px.business.manage.commodity.vo;


import com.px.common.api.base.vo.BaseVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

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
@ApiModel(value = "商品列表Vo", description = "商品列表Vo")
public class CommodityListVo extends BaseVo {

    @ApiModelProperty(value = "商品名称")
    private String commodityName;

    @ApiModelProperty(value = "商品封面图")
    private String coverUrl;

    @ApiModelProperty(value = "品牌名称")
    private String brandName;

    @ApiModelProperty(value = "商品轮播图(多张)")
    private String carouselUrl;

    @ApiModelProperty(value = "状态 0- 上架 1-下架")
    private Integer state;

    @ApiModelProperty(value = "店铺类型")
    private String shopType;

    @ApiModelProperty(value = "商品类型id")
    private String typeId;

    @ApiModelProperty(value = "商品类型名称")
    private String typeName;

    @ApiModelProperty(value = "排序")
    private Integer sort;


    @ApiModelProperty(value = "店长价")
    private BigDecimal shopownerPrice;

    @ApiModelProperty(value = "商品售价")
    private BigDecimal sellPrice;

    @ApiModelProperty(value = "拼团价")
    private BigDecimal groupWorkPrice;

//    @ApiModelProperty(value = "商品类型：1-普通商品 2-秒杀商品 3-预售商品 4-拼团商品")
//    private Integer commodityType;

    @ApiModelProperty(value = "是否推荐 0-不推荐 1-推荐")
    private Integer recommend;

    // app
    @ApiModelProperty(value = "商品原价")
    private BigDecimal originalPrice;

    // 管理端
    @ApiModelProperty(value = "商品库存")
    private Integer stock;


    // 普通商品无以下参数
    @ApiModelProperty(value = "拼团人数")
    private String groupNum;

    @ApiModelProperty(value = "开始时间")
    private LocalDateTime startTime;

    @ApiModelProperty(value = "结束时间")
    private LocalDateTime endTime;

    @ApiModelProperty(value = "店铺名字")
    private String shopName;

    @ApiModelProperty(value = "品牌id")
    private String brandId;
}