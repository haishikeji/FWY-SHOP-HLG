package com.px.business.manage.capital.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.Version;
import com.px.common.api.base.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.ToString;

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
@TableName("tb_capital")
@ApiModel(value = "Capital 对象", description = "平台/商家资金表")
public class Capital extends BaseEntity {

    private static final long serialVersionUID = -536777309840084998L;

    /**
     * 商家id（平台=0）
     */
    @TableField(value = "user_id")
    private String userId;

    /**
     * 金额
     */
    @TableField(value = "amount")
    private BigDecimal amount;

    /**
     * 乐观锁
     */
    @TableField(value = "version")
    @Version
    private Integer version;

}
