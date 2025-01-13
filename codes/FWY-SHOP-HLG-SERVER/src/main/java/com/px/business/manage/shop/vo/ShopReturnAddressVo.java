package com.px.business.manage.shop.vo;


import com.px.common.api.base.vo.BaseVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @@author 品讯科技
 * @Description: vo
 * @Date: 2021-03-05
 **/
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "店铺退货地址Vo", description = "店铺退货地址Vo")
public class ShopReturnAddressVo extends BaseVo {

    @ApiModelProperty(value = "店铺id")
    private String shopId;

    @ApiModelProperty(value = "收货人名称")
    private String consigneeName;

    @ApiModelProperty(value = "收货人电话")
    private String consigneePhone;

    @ApiModelProperty(value = "省")
    private String province;

    @ApiModelProperty(value = "市")
    private String city;

    @ApiModelProperty(value = "区")
    private String region;

    @ApiModelProperty(value = "地址")
    private String address;

    @ApiModelProperty(value = "默认地址 0- 默认 1-其他")
    private Integer type;


}