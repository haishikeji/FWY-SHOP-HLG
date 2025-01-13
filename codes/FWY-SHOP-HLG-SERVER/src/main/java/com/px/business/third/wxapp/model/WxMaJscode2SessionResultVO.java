package com.px.business.third.wxapp.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;


/**
 * 通过code获取openId和session key 返回对象
 * @author 品讯科技
 * @mail 1720696548@qq.com
 * @date 2024-08
 * @version 1.0.0
 */
@Data
@ToString(callSuper = true)
@ApiModel(value = "WxMaJscode2SessionResultVO 对象", description = "通过code获取openId和session key")
public class WxMaJscode2SessionResultVO implements Serializable {

    private static final long serialVersionUID = 7849102039100197284L;
    @ApiModelProperty(value = "用户唯一标识")
    private String openid;

    @ApiModelProperty(value = "会话密钥")
    private String sessionKey;

    @ApiModelProperty(value = "用户在开放平台的唯一标识符，在满足 UnionID 下发条件的情况下会返回")
    private String unionid;
}
