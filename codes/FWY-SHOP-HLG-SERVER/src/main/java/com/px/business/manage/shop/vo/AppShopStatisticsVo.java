package com.px.business.manage.shop.vo;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @@author 品讯科技
 * @Description: vo
 * @Date: 2021-02-24
 **/
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "app商铺统计Vo", description = "app商铺统计Vo")
public class AppShopStatisticsVo implements Serializable {

    @ApiModelProperty(value = "商品浏览数量")
    private Integer commodityViewNumber = 0;

//    @ApiModelProperty(value = "支付笔数")
//    private Integer paymentsNumber = 0;
//
//    @ApiModelProperty(value = "订单数量")
//    private Integer orderNumber = 0;
//
//    @ApiModelProperty(value = "支付总金额")
//    private BigDecimal totalAmountPaid = BigDecimal.ZERO;
//
//    @ApiModelProperty(value = "商户数量")
//    private Integer merchantNumber = 0;
//
//    @ApiModelProperty(value = "待支付订单数量")
//    private Integer unpaidNumber;
}