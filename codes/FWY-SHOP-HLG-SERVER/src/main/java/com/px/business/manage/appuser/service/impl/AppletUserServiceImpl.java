package com.px.business.manage.appuser.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.px.business.manage.appuser.dto.AppletUserDto;
import com.px.business.manage.appuser.entity.AppletUser;
import com.px.business.manage.appuser.mapper.AppletUserMapper;
import com.px.business.manage.appuser.service.IAppletUserService;
import com.px.business.manage.appuser.vo.AppletUserInfoVo;
import com.px.business.manage.appuser.vo.AppletUserVo;
import com.px.business.manage.appuser.vo.ManageAppletUserVo;
import com.px.common.exception.ErrorException;
import com.px.common.result.Result;
import com.px.common.result.ResultType;
import com.px.common.utils.BeanDtoVoUtils;
import com.px.common.utils.TokenUtil;
import com.px.sysuser.entity.SysUser;
import com.px.sysuser.service.ISysUserService;
import com.px.sysuser.vo.SysUserDetailsVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @@author 品讯科技
 * @Description: 小程序用户表 服务实现类
 * @Date: 2021-02-24
 **/
@Service
public class AppletUserServiceImpl extends ServiceImpl<AppletUserMapper, AppletUser> implements IAppletUserService {

    @Autowired
    private AppletUserMapper appletUserMapper;

    @Autowired
    private ISysUserService iSysUserService;

    @Autowired
    private HttpServletRequest request;

    @Override
    public AppletUserVo getAppletUserByUid(String openid) {
        if (StringUtils.isBlank(openid)) {
            return null;
        }
        //根据手机号码查询
        AppletUser appletUser = appletUserMapper.selectOne(
                new LambdaQueryWrapper<AppletUser>()
                        .eq(AppletUser::getOpenid, openid));
        return BeanDtoVoUtils.convert(appletUser, AppletUserVo.class);
    }

    @Override
    public Page<ManageAppletUserVo> queryManageAppletUserPage(Page<ManageAppletUserVo> page, Map<String, Object> map) {
        return appletUserMapper.queryManageAppletUserPage(page, map);
    }

    @Override
    public AppletUserInfoVo getAppletUserById(String userId) {
        if (StringUtils.isBlank(userId)) {
            return null;
        }
        //根据id查询
        AppletUser appletUser = appletUserMapper.selectById(userId);
        return BeanDtoVoUtils.convert(appletUser, AppletUserInfoVo.class);
    }

    @Override
    public Result setUpState(SysUserDetailsVo user, Integer state, String appletUserId) {
        AppletUser appletUser = appletUserMapper.selectById(appletUserId);
        if (appletUser == null) {
            throw new ErrorException(ResultType.OPERATION_DATA_IS_NULL);
        }
        AppletUser entity = new AppletUser();
        entity.setId(appletUserId);
        entity.setState(state);
        entity.setUpdateId(user.getId());
        boolean result = appletUserMapper.updateById(entity) > 0;
        return result ? Result.success() : Result.failure(ResultType.SYS_FAILURE_OPERATION);
    }


    @Override
    public Result updIsShopowner(SysUserDetailsVo user, Boolean isShopowner, String appletUserId) {
        AppletUser appletUser = appletUserMapper.selectById(appletUserId);
        if (appletUser == null) {
            throw new ErrorException(ResultType.OPERATION_DATA_IS_NULL);
        }
        AppletUser entity = new AppletUser();
        entity.setId(appletUserId);
        entity.setIsShopowner(isShopowner);
        entity.setUpdateId(user.getId());
        boolean result = appletUserMapper.updateById(entity) > 0;
        return result ? Result.success() : Result.failure(ResultType.SYS_FAILURE_OPERATION);
    }

    @Override
    public AppletUserInfoVo getAppletUserByToken(String token) {
        if (StringUtils.isNotBlank(token)) {
            String userId = TokenUtil.getUserId(token);
            // 查询用户信息
            AppletUser appletUser = appletUserMapper.selectById(userId);
            return BeanDtoVoUtils.convert(appletUser, AppletUserInfoVo.class);
        } else {
            return null;
        }
    }

    @Override
    public Result bindingPhone(String appletUserId, String phone) {
        AppletUser appletUser = appletUserMapper.selectById(appletUserId);
        if (appletUser == null) {
            throw new ErrorException(ResultType.USER_NOT_EXISTED);
        }
        AppletUser oldUser = appletUserMapper.selectOne(
                new LambdaQueryWrapper<AppletUser>()
                        .eq(AppletUser::getPhone, phone));
        if (oldUser != null && (!oldUser.getId().equals(appletUserId))) {
            throw new ErrorException(ResultType.PHONE_IS_EXIST);
        }
        AppletUser entity = new AppletUser();
        entity.setPhone(phone);
        entity.setId(appletUserId);
        boolean result = appletUserMapper.updateById(entity) > 0;
        return result ? Result.success() : Result.failure(ResultType.SYS_FAILURE_OPERATION);
    }

    @Override
    public Result updateUserInfo(AppletUserInfoVo appletUserInfoVo, AppletUserDto dto) {
        if (StringUtils.isBlank(appletUserInfoVo.getId())) {
            throw new ErrorException(ResultType.SELECT_OPERATION_DATA);
        }
        AppletUser appletUser = appletUserMapper.selectById(appletUserInfoVo.getId());
        if (appletUser == null) {
            throw new ErrorException(ResultType.OPERATION_DATA_IS_NULL);
        }
        AppletUser entity = BeanDtoVoUtils.convert(dto, AppletUser.class);
        entity.setId(appletUser.getId());
        entity.setUpdateId(appletUser.getId());
        boolean result = appletUserMapper.updateById(entity) > 0;
        return result ? Result.success() : Result.failure(ResultType.SYS_FAILURE_UPDATE);
    }


    @Override
    public Result<Boolean> bindSysUser(String loginName, String password) {
        // 验证用户
        SysUser user = iSysUserService.getOne(
                new LambdaQueryWrapper<SysUser>()
                        .eq(SysUser::getLoginName, loginName)
        );
        if (user == null) {
            throw new ErrorException(ResultType.ACCOUNT_NOT_EXIST);
        }
        if (user.getState() != 0) {
            throw new ErrorException(ResultType.ACCOUNT_FROZEN);
        }
        String pwd = DigestUtils.md5DigestAsHex(password.getBytes());
        if (!pwd.equals(user.getPassword())) {
            throw new ErrorException(ResultType.UNAUTHORIZED);
        }
        //
        String userId = TokenUtil.getUserId(request);
        AppletUser appletUser = new AppletUser();
        appletUser.setId(userId);
        appletUser.setShopId(user.getAboutId());
        this.updateById(appletUser);
        return Result.success(true);
    }
}

