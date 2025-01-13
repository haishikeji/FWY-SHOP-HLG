package com.px.business.third.pay.allpay.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.px.business.third.pay.allpay.model.entity.PayWalletFlow;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;

/**
 * 账单/流水/支付流水表
 * <p>
 *  ::本代码由[兮家小二]提供的代码生成器生成,如有问题,请手动修改 ::作者CSDN:https://blog.csdn.net/qq_41463655 
 * </p>
 * @author 品讯科技
 * @email zly@hnpinxun.cn
 * @date 2024-08
 */
public interface PayWalletFlowMapper extends BaseMapper<PayWalletFlow> {

    /**
     * 查询指定用户的总支出
     * @author 品讯科技
     * @param userId
     * @date 2024-08
     * @return java.math.BigDecimal
     * @version 1.0.0
     */
    BigDecimal findUserTotalAmount(@Param("userId") String userId);

}

