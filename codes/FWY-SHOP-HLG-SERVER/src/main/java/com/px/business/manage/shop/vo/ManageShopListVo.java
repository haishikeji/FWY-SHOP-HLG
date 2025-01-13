package com.px.business.manage.shop.vo;


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
@ApiModel(value = "管理端商铺列表Vo", description = "管理端商铺列表Vo")
public class ManageShopListVo implements Serializable {

    @ApiModelProperty(value = "店铺id")
    private String id;

    // 用户表
    @ApiModelProperty(value = "商户账号")
    private String loginName;

    // 用户表
    @ApiModelProperty(value = "商户用户id")
    private String userId;

    @ApiModelProperty(value = "店铺类型：1-自营 2-旗舰店 3-其他")
    private Integer shopType;

    @ApiModelProperty(value = "联系人")
    private String contactName;

    @ApiModelProperty(value = "联系人电话")
    private String contactsPhone;

    @ApiModelProperty(value = "商户所售类目id")
    private String saleTypeId;
    
    //类目表
    @ApiModelProperty(value = "商户所售类目")
    private String saleTypeName;

    @ApiModelProperty(value = "店铺名称")
    private String shopName;

    @ApiModelProperty(value = "商铺地址")
    private String shopAddress;

    @ApiModelProperty(value = "状态 0- 启用 1-禁用")
    private Integer state;
}