package com.px.sysuser.vo;

import com.px.common.api.base.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @@author 品讯科技
 * @Description: vo
 * @Date: 2020-05-26
 **/
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "系统用户列表Vo", description = "系统用户列表Vo")
public class SysUserVo extends BaseEntity {

    @ApiModelProperty(value = "用户名")
    private String nickname;

    @ApiModelProperty(value = "手机")
    private String phone;

    @ApiModelProperty(value = "状态 0：正常 1：禁用")
    private Integer state;

    @ApiModelProperty(value = "登陆名")
    private String loginName;

//    @ApiModelProperty(value = "企业id /平台id")
//    private String aboutId;

    @ApiModelProperty(value = "用户类型 0- 平台 1-商户")
    private Integer userType;

    @ApiModelProperty(value = "性别 0女 1男")
    private Integer sex;

    @ApiModelProperty(value = "0- 商家负责人 1-商家员工")
    private Integer type;

}