package com.px.business.third.pay.wxpay.service.impl;


import com.px.business.third.pay.wxpay.model.dto.WxEntPayDTO;
import com.px.business.third.pay.wxpay.model.vo.WxEntPayResultVO;
import com.px.common.result.Result;

/**
 * <pre>
 * 企业付款相关
 * Created by Binary Wang on 2018/9/27.
 * </pre>
 *
 *
 */

public interface XjEntPayService {


    /**
     * 企业打款
     * @param wxEntPayDTO
     * @return
     */
    public Result<WxEntPayResultVO> entPay(WxEntPayDTO wxEntPayDTO);
}
