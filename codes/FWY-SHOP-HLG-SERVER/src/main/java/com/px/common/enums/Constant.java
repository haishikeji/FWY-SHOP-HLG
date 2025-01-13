package com.px.common.enums;

/**
 * 基础静态变量
 */
public class Constant {

    /**
     * swagger 配置参数
     */
    public interface Info {
        String CURRENT_USER_TOKEN = "Authorization";//当前用户token,放在header
        String ENCODE_SALT = "zjb";//token盐值
    }

    /**
     * 图片验证码常量
     */
    public interface Identify {
        //图片验证码有效时间
        Long EXPIRE_TIME = 2L;
        //获取验证码的key
        String VERIFY_CODE = "verifyCode";
    }



}

