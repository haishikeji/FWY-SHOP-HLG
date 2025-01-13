package com.px.business.app.login.service;


import com.px.business.app.login.dto.LoginDTO;
import com.px.common.result.Result;

import java.util.Map;

/**
 * @@author 品讯科技
 * @Description: 小程序用户表 服务实现类
 * @Date: 2021-02-24
 **/
public interface AppletsAuthorizeService {

    Result<Map<String, Object>> weChatLogin(LoginDTO dto);
}
