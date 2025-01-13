package com.px.business.manage.commodity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import java.io.Serializable;

/**
 * @@author 品讯科技
 * @Description: vo
 * @Date: 2021-03-23
 **/
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="CommodityService数据传输对象", description="商品服务表")
public class CommodityServiceVo implements Serializable{

    @ApiModelProperty(value = "id")
    private String id;

    @ApiModelProperty(value = "服务名称")
    private String serviceName;

    @ApiModelProperty(value = "排序")
    private Integer sort;

    @ApiModelProperty(value = "状态 0 -正常 1-禁用")
    private Integer state;

    @ApiModelProperty(value = "服务描述")
    private String serviceDesc;


}