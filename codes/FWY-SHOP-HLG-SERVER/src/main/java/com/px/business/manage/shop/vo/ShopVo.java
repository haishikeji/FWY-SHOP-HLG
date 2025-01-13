package com.px.business.manage.shop.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import java.io.Serializable;
import java.util.List;

/**
 * @@author 品讯科技
 * @Description: vo
 * @Date: 2021-02-24
 **/
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "商铺Vo", description = "商铺Vo")
public class ShopVo implements Serializable {

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

    @ApiModelProperty(value = "微信号-电话号码")
    private String wechatPhone;

    @ApiModelProperty(value = "商户所售类目id")
    private String saleTypeId;

    //类目表
    @ApiModelProperty(value = "商户所售类目")
    private String saleTypeName;

    @ApiModelProperty(value = "店铺名称")
    private String shopName;

    @ApiModelProperty(value = "店铺logo")
    private String shopLogo;

    @ApiModelProperty(value = "店铺背景图url")
    private String shopBackground;

    @ApiModelProperty(value = "省")
    private String province;

    @ApiModelProperty(value = "市")
    private String city;

    @ApiModelProperty(value = "区")
    private String region;

    @ApiModelProperty(value = "商铺地址")
    private String shopAddress;

    @ApiModelProperty(value = "客服电话")
    private String customerServicePhone;

    @ApiModelProperty(value = "店铺纬度")
    private String longitude;

    @ApiModelProperty(value = "纬度")
    private String latitude;

    @ApiModelProperty(value = "主营内容")
    private String mainContent;

    @ApiModelProperty(value = "营业时间")
    private String businessHours;

    @ApiModelProperty(value = "店铺简介")
    private String shopProfile;

    @ApiModelProperty(value = "状态 0- 启用 1-禁用")
    private Integer state;

    @ApiModelProperty(value = "购买后退货时间(天)")
    private Integer returnTime;

    @ApiModelProperty(value = "确认收货时间(天)")
    private Integer receiveTime;

    // 平台账号查询店铺详情可看
    @ApiModelProperty(value = "角色列表")
    private List<String> roleList;
}