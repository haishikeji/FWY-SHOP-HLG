package com.px.business.manage.shop.vo;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @@author 品讯科技
 * @Description: vo
 * @Date: 2021-02-25
 **/
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "店铺申请列表Vo", description = "店铺申请列表Vo")
public class ShopApplyRecordListVo implements Serializable {

    //店铺表
    @ApiModelProperty(value = "店铺id")
    private String shopId;

    //店铺表
    @ApiModelProperty(value = "店铺名称")
    private String shopName;

    //店铺表
    @ApiModelProperty(value = "审批状态 0- 审批中 1-正常  2- 驳回  3-禁用")
    private Integer approvalStatus;

    @ApiModelProperty(value = "申请人id")
    private String applyId;

    @ApiModelProperty(value = "申请人名称")
    private String applyName;

    @ApiModelProperty(value = "申请时间")
    private LocalDateTime createTime;

    //店铺表
    @ApiModelProperty(value = "联系人")
    private String contactName;

    //店铺表
    @ApiModelProperty(value = "联系人电话")
    private String contactsPhone;

    @ApiModelProperty(value = "备注")
    private String remarks;

    @ApiModelProperty(value = "审核时间")
    private LocalDateTime auditTime;

    // 用户表
    @ApiModelProperty(value = "商户账号")
    private String loginName;

    // 用户表
    @ApiModelProperty(value = "商户用户id")
    private String userId;


}