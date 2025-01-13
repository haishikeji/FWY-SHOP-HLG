package com.px.business.app.order.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="ManagerSaleVO", description="售后订单")
public class ManagerSaleVO {

    @ApiModelProperty("售后订单id")
    private String id;

    @ApiModelProperty("原订单id")
    private String orderId;

    @ApiModelProperty("订单编号")
    private String orderNo;

    @ApiModelProperty("收货人")
    private String userName;

    @ApiModelProperty("联系方式")
    private String phone;

    @ApiModelProperty("售后状态")
    private Integer state;

    @ApiModelProperty("商品规格名称")
    private String specsName;

    @ApiModelProperty("规格图片")
    private String  specsPicture;

    @ApiModelProperty("规格单价")
    private BigDecimal specsPrice;

    @ApiModelProperty("数量")
    private Integer num;

    @ApiModelProperty("总价")
    private BigDecimal totalPrice;

    @ApiModelProperty("实际价格")
    private BigDecimal actualPrice;

    @ApiModelProperty("申请时间")
    private LocalDateTime createTime;

    @ApiModelProperty("审核时间")
    private LocalDateTime examineTime;


}
