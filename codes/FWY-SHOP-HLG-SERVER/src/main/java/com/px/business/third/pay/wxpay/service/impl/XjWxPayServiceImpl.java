package com.px.business.third.pay.wxpay.service.impl;

import com.github.binarywang.wxpay.bean.notify.WxPayOrderNotifyResult;
import com.github.binarywang.wxpay.bean.order.WxPayMpOrderResult;
import com.github.binarywang.wxpay.bean.request.WxPayRefundRequest;
import com.github.binarywang.wxpay.bean.request.WxPayUnifiedOrderRequest;
import com.github.binarywang.wxpay.bean.result.WxPayRefundResult;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.px.business.third.pay.wxpay.wxApi.WxPayApi;
import com.px.business.third.pay.wxpay.service.XjWxPayService;
import com.px.business.third.pay.wxpay.model.dto.WxPayOrderDTO;
import com.px.business.third.pay.wxpay.model.dto.WxPayRefundDTO;
import com.px.business.third.pay.wxpay.model.vo.WxPayOrderNotifyResultVO;
import com.px.business.third.pay.wxpay.model.vo.WxPayOrderResultVO;
import com.px.business.third.pay.wxpay.model.vo.WxPayRefundResultVO;
import com.px.common.result.Result;
import com.px.common.result.ResultType;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;


/**
 * <pre>
 * 微信支付相关
 * Created by Binary Wang on 2018/9/27.
 * </pre>
 */
@Service
@Slf4j
public class XjWxPayServiceImpl implements XjWxPayService {

    @Autowired
    private WxPayApi wxPayApi;

    @Autowired
    private HttpServletRequest request;

    /**
     * 支付下单
     * @author 品讯科技
     * @param dto
     * @date 2024-08
     * @return com.ws.ldy.others.wechat.pay.model.vo.WxPayMpOrderResultVO
     * @version 1.0.0
     */
    @Override
    public Result<WxPayOrderResultVO> createOrder(WxPayOrderDTO dto) {
        if (StringUtils.isBlank(dto.getOpenid())) {
            return Result.failure(ResultType.WX_PAY_NO_OPENID);
        }
        // 默认支付方式
        if (StringUtils.isBlank(dto.getTradeType())) {
            dto.setTradeType("JSAPI");
        }
        // 默认商品描叙
        if (StringUtils.isBlank(dto.getBody())) {
            dto.setBody("暂无商品描叙");
        }
        WxPayUnifiedOrderRequest orderRequest = new WxPayUnifiedOrderRequest();
        //参数配置
        String body = dto.getBody();
        orderRequest.setNotifyUrl(dto.getNotifyUrl());
        orderRequest.setBody(body);
        orderRequest.setOpenid(dto.getOpenid());
        orderRequest.setOutTradeNo(dto.getOutTradeNo());
        orderRequest.setTotalFee(dto.getTotalFee());
        orderRequest.setTradeType(dto.getTradeType());
        orderRequest.setSpbillCreateIp(request.getRemoteHost());
        try {
            WxPayMpOrderResult result = wxPayApi.createOrder(orderRequest);
            WxPayOrderResultVO vo = new WxPayOrderResultVO();
            BeanUtils.copyProperties(result, vo);
            return Result.success(vo);
        } catch (WxPayException e) {
            log.debug(e.toString());
            return Result.failure(ResultType.WX_PAY_FAILURE.getCode(), ResultType.WX_PAY_FAILURE.getMsg() + ":" + e.getReturnMsg() + ":" + e.getErrCodeDes());
        }
    }


    /**
     * 退款
     * @author 品讯科技
     * @param dto
     * @date 2024-08
     * @return com.github.binarywang.wxpay.bean.result.WxRefundResultVO
     * @version 1.0.0
     */
    @Override
    public Result<WxPayRefundResultVO> refund(WxPayRefundDTO dto) {
        WxPayRefundRequest refundRequest = new WxPayRefundRequest();
        refundRequest.setOutTradeNo(dto.getOutTradeNo());
        refundRequest.setOutRefundNo(dto.getOutRefundNo());
        refundRequest.setTotalFee(dto.getTotalFee());
        refundRequest.setRefundFee(dto.getRefundFee());
        refundRequest.setRefundDesc(dto.getRefundDesc());
        WxPayRefundResultVO vo = new WxPayRefundResultVO();
        try {
            WxPayRefundResult refund = wxPayApi.refund(refundRequest);
            BeanUtils.copyProperties(refund, vo);
            return Result.success(vo);
        } catch (WxPayException e) {
            log.debug(e.toString());
            return Result.failure(ResultType.WX_PAY_FAILURE.getCode(),
                    ResultType.WX_PAY_FAILURE.getMsg()
                            + ":" + e.getReturnMsg()
                            + ":" + e.getCustomErrorMsg()
                            + ":" + e.getErrCodeDes());
        }
    }


    /**
     * 获取支付回调数据
     * @param xmlData
     * @return
     */
    @Override
    public Result<WxPayOrderNotifyResultVO> parseOrderNotifyResult(String xmlData) {
        WxPayOrderNotifyResultVO vo = new WxPayOrderNotifyResultVO();
        try {
            WxPayOrderNotifyResult result = wxPayApi.parseOrderNotifyResult(xmlData);
            BeanUtils.copyProperties(result, vo);
        } catch (WxPayException e) {
            BeanUtils.copyProperties(e, vo);
        }
        return Result.success(vo);
    }
}

