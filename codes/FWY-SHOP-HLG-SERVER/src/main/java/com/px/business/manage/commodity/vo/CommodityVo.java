package com.px.business.manage.commodity.vo;


import com.px.common.api.base.vo.BaseVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @@author 品讯科技
 * @Description: vo
 * @Date: 2021-03-01
 **/
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "CommodityVo", description = "商品详情Vo")
public class CommodityVo extends BaseVo {

    @ApiModelProperty(value = "店铺id")
    private String shopId;

    @ApiModelProperty(value = "商品名称")
    private String commodityName;

    @ApiModelProperty(value = "商城商品编码 ")
    private String commodityCode;

    @ApiModelProperty(value = "副标题")
    private String subTitle;

    @ApiModelProperty(value = "商品封面图")
    private String coverUrl;

    @ApiModelProperty(value = "商品轮播图(多张)")
    private String carouselUrl;

    @ApiModelProperty(value = "商品详情")
    private String commodityDetail;

    @ApiModelProperty(value = "状态 0- 上架 1-下架")
    private Integer state;

    @ApiModelProperty(value = "商品类型id")
    private String typeId;

    @ApiModelProperty(value = "商品类型名称")
    private String typeName;

    @ApiModelProperty(value = "排序")
    private Integer sort;

    @ApiModelProperty(value = "分享标题")
    private String shareTitle;

    @ApiModelProperty(value = "分享图片地址")
    private String sharePicture;

    @ApiModelProperty(value = "是否推荐 0-不推荐 1-推荐")
    private Integer recommend;

    @ApiModelProperty(value = "商品类型：1-普通商品 2-秒杀商品 3-预售商品 4-拼团商品")
    private Integer commodityType;

    @ApiModelProperty(value = "品牌Id")
    private String brandId;

    @ApiModelProperty(value = "服务列表")
    private String serviceId;

    @ApiModelProperty(value = "商品规格列表")
    private List<CommoditySpecificationVo> voList;


    // 普通商品无以下参数
    @ApiModelProperty(value = "拼团人数")
    private Integer groupNum;

    @ApiModelProperty(value = "限购数量")
    private Integer limitedQuantity;

    @ApiModelProperty(value = "开始时间")
    private LocalDateTime startTime;

    @ApiModelProperty(value = "结束时间")
    private LocalDateTime endTime;

}