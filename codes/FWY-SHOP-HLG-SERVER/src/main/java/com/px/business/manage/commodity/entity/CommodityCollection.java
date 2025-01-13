package com.px.business.manage.commodity.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.px.common.api.base.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @@author 品讯科技
 * @Description: entity
 * @Date: 2021-03-02
 **/
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "CommodityCollection", description = "商品收藏表")
@TableName("tb_commodity_collection")
public class CommodityCollection extends BaseEntity {

    @ApiModelProperty(value = "小程序用户id")
    @TableField("user_id")
    private String userId;

    @ApiModelProperty(value = "商品id")
    @TableField("commodity_id")
    private String commodityId;

    @ApiModelProperty(value = "类型 商品-0  专题1")
    @TableField("type")
    private Integer type;


}