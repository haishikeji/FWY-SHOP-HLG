package com.px.business.manage.commodity.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;

/**
 * @@author 品讯科技
 * @Description: CommodityDTO
 * @Date: 2021-03-01
 **/
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "修改商品Dto", description = "修改商品Dto")
public class UpdateCommodityDto implements Serializable {

    @ApiModelProperty(value = "商品id-修改必传")
    private String commodityId;

    @ApiModelProperty(value = "商品名称", required = true)
    @NotBlank(message = "商品名称不能为空")
    private String commodityName;

    @ApiModelProperty(value = "设置的已出售数量")
    private Integer soldQuantity;

    @ApiModelProperty(value = "商城商品编码")
    private String commodityCode;

    @ApiModelProperty(value = "副标题")
    private String subTitle;

    @ApiModelProperty(value = "商品视频")
    private String videoUrl;

    @ApiModelProperty(value = "商品轮播图(多张)", required = true)
    @NotBlank(message = "轮播图不能为空")
    private String carouselUrl;

    @ApiModelProperty(value = "商品详情")
    private String commodityDetail;

    @ApiModelProperty(value = "发货方式 0-快递配送 1- 到店自提", required = true)
    @NotBlank(message = "发货方式不能为空")
    private String deliveryMethod;

    @ApiModelProperty(value = "商品类型id", required = true)
    @NotBlank(message = "商品类型id不能为空")
    private String typeId;

    @ApiModelProperty(value = "商品类型名称")
    private String typeName;

    @ApiModelProperty(value = "排序")
    private Integer sort;

    @ApiModelProperty(value = "起售数量", required = true)
    @NotNull(message = "起售数量不能为空")
    private Integer startQuantity;

    @ApiModelProperty(value = "是否包邮  0-包邮 1-不包邮", required = true)
    @NotNull(message = "是否包邮不能为空")
    private Integer freight;

    @ApiModelProperty(value = "运费规则id")
    private String freightRules;

    @ApiModelProperty(value = "分享标题")
    private String shareTitle;

    @ApiModelProperty(value = "分享图片地址")
    private String sharePicture;


    @ApiModelProperty(value = "商品规格", required = true)
    @NotNull(message = "商品规格不能为空")
    @Valid // 嵌套验证必须用@Valid
    @Size(min = 1, message = "至少设置一个规格")
    private List<UpdateCommoditySpecificationDto> specificationDtoList;


}