package com.px.business.manage.statistics.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "DayTotalVO", description = "")
public class DayTotalVO implements Serializable {


    private static final long serialVersionUID = 388367496694101166L;
    @ApiModelProperty(value = "今日销售额", required = true)
    private BigDecimal daySales;

    @ApiModelProperty(value = "今日订单数量", required = true)
    private Integer dayOrderNum;

    @ApiModelProperty(value = "待支付数量", required = true)
    private Integer dayToBePaidNum;

    @ApiModelProperty(value = "待发货数量", required = true)
    private Integer dayForwardingNum;
}
