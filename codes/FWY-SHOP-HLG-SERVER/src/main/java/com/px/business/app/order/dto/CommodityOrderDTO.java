package com.px.business.app.order.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Accessors(chain = true)
@ApiModel
public class CommodityOrderDTO {

    @ApiModelProperty(value = "商品id")
    @NotBlank(message = "商品id不能为空")
    private String goodId;

    @ApiModelProperty(value = "商品规格id")
    @NotBlank(message = "商品规格id不能为空")
    private String specificationId;

    @ApiModelProperty(value = "商品数量")
    @NotNull
    @Min(value = 1,message = "至少为1")
    private Integer num;

    @ApiModelProperty(value = "订单备注")
    private String orderNote;

    @ApiModelProperty(value = "发货方式")
    @NotNull(message = "请确认发货方式 0-快递配送 1- 到店自提 2-及时送")
    private Integer deliveryMode;

    @ApiModelProperty(value = "收件人  收货方式为自提再填")
    private String pickName;

    @ApiModelProperty(value = "收件人号码 收货方式为自提再填")
    private String pickPhone;

    /*@ApiModelProperty(value = "商品类型 1-普通商品 2-秒杀 3-预售 4-拼团")
    private Integer goodsType;*/

    @ApiModelProperty(value = "团购购买商品购买方式  0-单独支付 1-团购")
    @NotNull(message = "请确认购买方式")
    private Integer way;

    @ApiModelProperty(value = "开团号 如果该商品已开团则必穿开团号，否则不传")
    @NotBlank
    private String groupWorkNo;


}
