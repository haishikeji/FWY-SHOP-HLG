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
 * @Date: 2021-02-25
 **/
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "店铺所售类目Vo", description = "店铺所售类目Vo")
public class ShopSaleCategoryVo extends BaseVo {

    @ApiModelProperty(value = "店铺所售类目名称")
    private String name;

    @ApiModelProperty(value = "状态 0-正常 1-禁用")
    private Integer state;

    @ApiModelProperty(value = "描述、备注")
    private String remarks;


}