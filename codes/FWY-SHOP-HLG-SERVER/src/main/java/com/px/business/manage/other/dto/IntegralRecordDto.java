package com.px.business.manage.other.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import java.io.Serializable;

/**
 * @@author 品讯科技
 * @Description: IntegralRecordDTO
 * @Date: 2021-03-15
 **/
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="IntegralRecord数据传输对象", description="积分记录")
public class IntegralRecordDto implements Serializable{

    @ApiModelProperty(value = "主订单id")
    private String masterOrder;

    @ApiModelProperty(value = "商品类型 0-实物 1-虚拟")
    private Integer commodityType;

    @ApiModelProperty(value = "积分类型 0- 积分 1-抵扣")
    private Integer type;

    @ApiModelProperty(value = "之前的积分")
    private Integer previousPoints;

    @ApiModelProperty(value = "之后的积分")
    private Integer afterPoints;

    @ApiModelProperty(value = "本次积分")
    private Integer points;

    @ApiModelProperty(value = "用户id")
    private String userId;


}