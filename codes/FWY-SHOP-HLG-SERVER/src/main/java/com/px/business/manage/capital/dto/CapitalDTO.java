package com.px.business.manage.capital.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 平台/商家资金表
 * <p>
 *  ::本代码由[兮家小二]提供的代码生成器生成,如有问题,请手动修改 ::作者CSDN:https://blog.csdn.net/qq_41463655
 * </p>
 * @author 品讯科技
 * @email zly@hnpinxun.cn
 * @date 2024-08
 */
@Data
@ToString(callSuper = true)
@ApiModel(value = "CapitalDTO 对象", description = "平台/商家资金表")
public class CapitalDTO implements Serializable {

    private static final long serialVersionUID = -536777309890416658L;

    @ApiModelProperty(notes = "商家id（平台=0）", position = 0)
    private String userId;

    @ApiModelProperty(notes = "金额", position = 1)
    @DecimalMin(value = "0", message = "金额 必须>=0")
    @DecimalMax(value = "9999999999", message = "金额 必须<=9999999999")
    private BigDecimal amount;

    @ApiModelProperty(notes = "乐观锁", position = 2)
    private Integer version;
}
