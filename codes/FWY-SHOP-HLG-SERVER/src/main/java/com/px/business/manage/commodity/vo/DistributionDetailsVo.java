package com.px.business.manage.commodity.vo;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

/**
 * @@author 品讯科技
 * @Description: vo
 * @Date: 2021-03-04
 **/
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "商品分销详情Vo", description = "商品分销详情Vo")
public class DistributionDetailsVo implements Serializable {


    @ApiModelProperty(value = "商品id")
    private String commodityId;

    @ApiModelProperty(value = "商品是否分销 0-不分销 1-分销")
    private Integer distribution;

    @ApiModelProperty(value = "佣金类型  0-固定金额 1- 百分比")
    private Integer commissionType;

    @ApiModelProperty(value = "规则分销列表")
    private List<CommodityDistributionVo> distributionVoList;

}