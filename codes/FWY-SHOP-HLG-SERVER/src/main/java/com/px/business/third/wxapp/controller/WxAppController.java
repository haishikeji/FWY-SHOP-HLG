package com.px.business.third.wxapp.controller;


import com.px.business.third.wxapp.model.WxMaJscode2SessionResultVO;
import com.px.business.third.wxapp.model.WxMaPhoneNumberInfoVO;
import com.px.business.third.wxapp.server.WxAppService;
import com.px.common.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * 微信网页授权
 * @author 品讯科技
 * @date 2024-08
 * @return
 * @version 1.0.0
 */
@RestController
@RequestMapping("/api/open/wx/app")
@Api(value = "WxAppController", tags = "WX  -->  APP小程序")
public class WxAppController {

    @Autowired
    private WxAppService wxAppService;


    @RequestMapping(value = "/auth/login", method = RequestMethod.GET)
    @ApiOperation(value = "小程序登录, 通过code 获取openId以及sessionKey",
            notes = "详见微信文档下的 auth.code2Session 接口说明：https://developers.weixin.qq.com/miniprogram/dev/framework/open-ability/login.html")
    public Result<WxMaJscode2SessionResultVO> login(@RequestParam String code) {
        return wxAppService.login(code);
    }


    @RequestMapping(value = "/auth/phone", method = RequestMethod.GET)
    @Deprecated
    @ApiOperation(value = "获取微信绑定的手机号",
            notes = "1、先登录获取sessionKey 存放到本地\r\n" +
                    "2、小程序端使用 button 组件 open-type 的值设置为 getPhoneNumber，让用户点击并同意获取参数\r\n" +
                    "3、将获取到的参数 encryptedData + iv 以及sessionKey传递到本接口,返回微信绑定的手机号" +
                    "详见微信文档：https://developers.weixin.qq.com/miniprogram/dev/framework/open-ability/getPhoneNumber.html")
    public Result<WxMaPhoneNumberInfoVO> phone(@RequestParam String sessionKey,
                                          @RequestParam String encryptedData,
                                          @RequestParam String iv) {
        return wxAppService.phone(sessionKey, encryptedData, iv);
    }

    @RequestMapping(value = "/auth/getWxUserPhone", method = RequestMethod.GET)
    @ApiOperation(value = "获取微信绑定的手机号",
            notes = "1、先登录获取sessionKey 存放到本地\r\n" +
                    "2、小程序端使用 button 组件 open-type 的值设置为 getPhoneNumber，让用户点击并同意获取参数\r\n" +
                    "3、将获取到的参数 encryptedData + iv 以及sessionKey传递到本接口,返回微信绑定的手机号" +
                    "详见微信文档：https://developers.weixin.qq.com/miniprogram/dev/framework/open-ability/getPhoneNumber.html")
    public Result<WxMaPhoneNumberInfoVO> getWxUserPhone(@RequestParam String code) {
        return wxAppService.authPhone(code);
    }


    @RequestMapping(value = "/qrcode/create", method = RequestMethod.GET)
    @ApiOperation(value = "获取图形太阳二维码(返回base64图片)",
            notes = "详见微信文档：https://developers.weixin.qq.com/miniprogram/dev/api-backend/open-api/qr-code/wxacode.getUnlimited.html")
    public Result<String> createQrCode(@RequestParam String scene,
                                  @RequestParam String page,
                                  @RequestParam Integer width,
                                  @RequestParam Boolean autoColor,
                                  @RequestParam Boolean isHyaline) {
        return wxAppService.createQrCode(scene, page, width, autoColor, isHyaline);
    }


    @RequestMapping(value = "/subscribe/sendMsgTest", method = RequestMethod.POST)
    @ApiOperation(value = "订阅消息发送测试", notes = "" +
            "1、需在微信小程序中配置模板： 微信公众平台 -> 小程序 -> 功能 -> 订阅消息 \r\n" +
            "2、小程序需发起订阅请求,用户点击同意才能发送成功\r\n" +
            "3、详见微信文档：https://developers.weixin.qq.com/miniprogram/dev/framework/open-ability/subscribe-message.html")
    public Result<String> sendMsg(@RequestParam String openId) {
        // 模板Id
        String templateId = "mZlYMEKvhF1qFqjJE88B4PdHXQprWLyQh2DbLzMCCTs";
        // 模板参数
        Map<String, String> sendMsg = new HashMap<>();
        sendMsg.put("thing1", "参数1");
        sendMsg.put("thing2", "参数2");
        // 发送
        return wxAppService.sendMsg(openId, templateId, sendMsg);
    }
}