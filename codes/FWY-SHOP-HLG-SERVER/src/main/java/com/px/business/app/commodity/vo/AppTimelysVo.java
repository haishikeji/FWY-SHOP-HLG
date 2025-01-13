package com.px.business.app.commodity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "AppTimelyVo", description = "及时送")
public class AppTimelysVo implements Serializable {

    private static final long serialVersionUID = -1100236757231186346L;
    @ApiModelProperty(value = "店铺id")
    private String shopId;

    @ApiModelProperty(value = "店铺名称")
    private String shopName;

    @ApiModelProperty(value = "店铺背景图url")
    private String shopBackground;

    @ApiModelProperty(value = "商铺地址")
    private String shopAddress;

    @ApiModelProperty(value = "营业时间")
    private String businessHours;

    @ApiModelProperty(value = "预计送达时间(单位分)")
    private Integer estimatedTime;

    @ApiModelProperty(value = "距离(公里保留两位小数)")
    private Double distance;
}
