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
 * @Date: 2021-02-26
 **/
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "商品分类列表Vo", description = "商品分类列表Vo")
public class CommodityClassificationListVo implements Serializable {

    @ApiModelProperty(value = "id")
    private String id;

    @ApiModelProperty(value = "分类名称")
    private String classificationName;
    
}