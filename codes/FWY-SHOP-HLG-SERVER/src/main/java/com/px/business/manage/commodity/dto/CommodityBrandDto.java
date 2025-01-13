package com.px.business.manage.commodity.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @@author 品讯科技
 * @Description: CommodityBrandDTO
 * @Date: 2021-03-22
 **/
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="CommodityBrandDto数据传输对象", description="商品品牌表")
public class CommodityBrandDto implements Serializable{

    @ApiModelProperty(value = "id-修改必传")
    private String id;

    @ApiModelProperty(value = "品牌名称",required = true)
    @NotNull(message = "品牌名称不能为空")
    private String brandName;

    @ApiModelProperty(value = "排序")
    private Integer sort;

    @ApiModelProperty(value = "品牌描述")
    private String brandDesc;


}