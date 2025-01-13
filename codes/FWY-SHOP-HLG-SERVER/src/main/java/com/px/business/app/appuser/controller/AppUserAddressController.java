package com.px.business.app.appuser.controller;

import com.px.business.app.appuser.dto.UserAddressDto;
import com.px.business.app.appuser.service.IUserAddressService;
import com.px.business.app.appuser.vo.UserAddressVo;
import com.px.business.manage.appuser.service.IAppletUserService;
import com.px.business.manage.appuser.vo.AppletUserInfoVo;
import com.px.common.exception.ErrorException;
import com.px.common.result.Result;
import com.px.common.result.ResultType;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * @@author 品讯科技
 * @Description: 收货地址表 前端控制器
 * @Date: 2021-02-25
 **/
@RestController
@RequestMapping("/app/userAddress")
@Api(value = "/app/userAddress", tags = "收货地址表")
public class AppUserAddressController {

    @Autowired
    private IUserAddressService userAddressService;

    @Autowired
    private IAppletUserService appletUserService;

    @PostMapping(value = "/addUserAddress")
    @ApiOperation("添加收货地址")
    public Result addUserAddress(@RequestHeader(value = "Authorization",required = false) String token,
                                     @Valid @RequestBody UserAddressDto dto){
        AppletUserInfoVo appletUserInfoVo = appletUserService.getAppletUserByToken(token);
        if (appletUserInfoVo == null) {
            throw new ErrorException(ResultType.USER_IS_NOT_LOGIN);
        }
        return userAddressService.addUserAddress(appletUserInfoVo,dto);
    }

    @PutMapping(value = "/modifyUserAddress")
    @ApiOperation("编辑收货地址")
    public Result modifyUserAddress(@RequestHeader(value = "Authorization",required = false) String token,
                                        @Valid @RequestBody UserAddressDto dto){
        AppletUserInfoVo appletUserInfoVo = appletUserService.getAppletUserByToken(token);
        if (appletUserInfoVo == null) {
            throw new ErrorException(ResultType.USER_IS_NOT_LOGIN);
        }
        return userAddressService.modifyUserAddress(appletUserInfoVo,dto);
    }

    @DeleteMapping(value = "/deleteUserAddress")
    @ApiOperation("删除收货地址")
    public Result deleteUserAddress(@RequestHeader(value = "Authorization",required = false) String token,
                                        @RequestParam(value = "userAddressId") String userAddressId){
        AppletUserInfoVo appletUserInfoVo = appletUserService.getAppletUserByToken(token);
        if (appletUserInfoVo == null) {
            throw new ErrorException(ResultType.USER_IS_NOT_LOGIN);
        }
        return userAddressService.deleteUserAddress(userAddressId);
    }

    @GetMapping(value = "/queryUserAddressInfo")
    @ApiOperation("查询收货地址详情")
    public Result<UserAddressVo> queryUserAddressInfo(@RequestHeader(value = "Authorization",required = false) String token,
                                                      @RequestParam(value = "userAddressId") String userAddressId){
        AppletUserInfoVo appletUserInfoVo = appletUserService.getAppletUserByToken(token);
        if (appletUserInfoVo == null) {
            throw new ErrorException(ResultType.USER_IS_NOT_LOGIN);
        }
        return Result.success(userAddressService.queryUserAddressInfo(userAddressId));
    }

    @GetMapping(value = "/queryUserAddressList")
    @ApiOperation("查询收货地址列表")
    public Result<List<UserAddressVo>> queryUserAddressList(@RequestHeader(value = "Authorization",required = false) String token){
        AppletUserInfoVo appletUserInfoVo = appletUserService.getAppletUserByToken(token);
        if (appletUserInfoVo == null) {
            throw new ErrorException(ResultType.USER_IS_NOT_LOGIN);
        }
        return Result.success(userAddressService.queryUserAddressList(appletUserInfoVo.getId()));
    }

    @PutMapping(value = "/setUpDefaultAddress")
    @ApiOperation("设置默认收货地址")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userAddressId",value = "收货地址id",required = true)
    })
    public Result setUpDefaultAddress(@RequestHeader(value = "Authorization",required = false) String token,
                                    @RequestParam(value = "userAddressId") String userAddressId){
        AppletUserInfoVo appletUserInfoVo = appletUserService.getAppletUserByToken(token);
        if (appletUserInfoVo == null) {
            throw new ErrorException(ResultType.USER_IS_NOT_LOGIN);
        }
        return userAddressService.setUpDefaultAddress(appletUserInfoVo,userAddressId);
    }


    @GetMapping(value = "/queryDefaultUserAddressInfo")
    @ApiOperation("查询默认收货地址详情")
    public Result<UserAddressVo> queryDefaultUserAddressInfo(@RequestHeader(value = "Authorization",required = false) String token){
        AppletUserInfoVo appletUserInfoVo = appletUserService.getAppletUserByToken(token);
        if (appletUserInfoVo == null) {
            throw new ErrorException(ResultType.USER_IS_NOT_LOGIN);
        }
        return Result.success(userAddressService.queryDefaultUserAddressInfo(appletUserInfoVo.getId()));
    }
}
