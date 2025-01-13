package com.px.business.app.login.service.impl;

import com.px.business.app.login.dto.LoginDTO;
import com.px.business.app.login.service.AppletsAuthorizeService;
import com.px.business.manage.appuser.entity.AppletUser;
import com.px.business.manage.appuser.service.IAppletUserService;
import com.px.business.manage.appuser.vo.AppletUserVo;
import com.px.common.constant.UserConstant;
import com.px.common.exception.ErrorException;
import com.px.common.result.Result;
import com.px.common.result.ResultType;
import com.px.common.utils.BeanDtoVoUtils;
import com.px.common.utils.JWTUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName AppletsAuthorizeServiceImpl
 * @Description
 * @author 品讯科技
 * @date 2024-08
 * @Version 1.0
 */
@Service
@Slf4j
public class AppletsAuthorizeServiceImpl implements AppletsAuthorizeService {


    @Autowired
    private IAppletUserService appletUserService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public synchronized Result<Map<String, Object>> weChatLogin(LoginDTO dto) {
        //验证当前用户是否存在
        AppletUserVo appletUserVo = appletUserService.getAppletUserByUid(dto.getOpenId());
        //授权绑定患者
        if (appletUserVo == null) {
            //不存在则新建
            AppletUser entity = new AppletUser();
            entity.setNickname(dto.getNickName());
            entity.setHeadUrl(dto.getAvatarUrl());
            entity.setOpenid(dto.getOpenId());
            entity.setSex(dto.getGender());
            entity.setPhone(dto.getPhone());
            entity.setState(UserConstant.UserState.NORMAL);
            appletUserService.save(entity);
            appletUserVo = BeanDtoVoUtils.convert(entity, AppletUserVo.class);
        }else{
            if(StringUtils.isBlank(appletUserVo.getPhone())
                    && StringUtils.isNotBlank(dto.getPhone())){
                //如果手机号为空更新手机号
                appletUserVo.setPhone(dto.getPhone());
                AppletUser user = new AppletUser();
                user.setId(appletUserVo.getId());
                user.setPhone(dto.getPhone());
                appletUserService.updateById(user);
            }
        }
        Integer state = appletUserVo.getState();
        if(!UserConstant.UserState.NORMAL.equals(state)){
            throw new ErrorException(ResultType.ACCOUNT_FROZEN);
        }
        String token = JWTUtils.createToken(appletUserVo.getId());
        Map<String, Object> map = new HashMap<>();
        map.put("token", token);
        map.put("loginAppletUserVo", appletUserVo);
        return Result.success(map);
    }
}
