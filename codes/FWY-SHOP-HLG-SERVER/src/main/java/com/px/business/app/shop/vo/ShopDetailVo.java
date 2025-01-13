package com.px.business.app.shop.vo;

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
@ApiModel(value = "店铺信息", description = "店铺信息")
public class ShopDetailVo implements Serializable {

    @ApiModelProperty(value = "店铺id")
    private String id;

    @ApiModelProperty(value = "店铺名称")
    private String shopName;

    @ApiModelProperty(value = "店铺纬度")
    private String longitude;

    @ApiModelProperty(value = "纬度")
    private String latitude;
}