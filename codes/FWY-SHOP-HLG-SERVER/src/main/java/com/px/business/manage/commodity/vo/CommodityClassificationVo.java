package com.px.business.manage.commodity.vo;


import com.px.common.api.base.vo.BaseVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @@author 品讯科技
 * @Description: vo
 * @Date: 2021-02-26
 **/
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "商品分类Vo", description = "商品分类Vo")
public class CommodityClassificationVo extends BaseVo {

    @ApiModelProperty(value = "分类名称")
    private String classificationName;

    @ApiModelProperty(value = "排序")
    private Integer sort;

    @ApiModelProperty(value = "状态 0 -正常 1-禁用")
    private Integer state;

    @ApiModelProperty(value = "类型描述")
    private String classificationDesc;


}