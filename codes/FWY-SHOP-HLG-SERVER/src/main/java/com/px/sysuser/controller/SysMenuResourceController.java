package com.px.sysuser.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.px.common.exception.ErrorException;
import com.px.common.result.Result;
import com.px.common.result.ResultType;
import com.px.common.utils.BeanDtoVoUtils;
import com.px.sysuser.entity.SysResource;
import com.px.sysuser.service.ISysResourceService;
import com.px.sysuser.service.ISysUserService;
import com.px.sysuser.service.ScanMappings;
import com.px.sysuser.vo.SysResourceVo;
import com.px.sysuser.vo.SysUserDetailsVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @@author 品讯科技
 * @Description: 菜单资源关系表 前端控制器
 * @Date: 2020-05-29
 **/
@RestController
@RequestMapping("/sysMenuResource")
@Api(value = "/sysMenuResource", tags = "菜单资源关系表")
public class SysMenuResourceController {

    @Autowired
    private ISysResourceService sysResourceService;

    @Autowired
    private ISysUserService iSysUserService;

    @Autowired
    private ScanMappings scanMappings;

    /**
     * 分页模糊查询资源
     *
     * @param current
     * @param size
     * @param resourceName
     * @param method
     * @param authType
     * @return
     */
    @RequestMapping(value = "/getResourcePage", method = RequestMethod.GET)
    @ApiOperation("分页模糊查询资源")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "resourceName", value = "需要查询的资源名", paramType = "query"),
            @ApiImplicitParam(name = "method", value = "需要查询的请求方式", paramType = "query"),
            @ApiImplicitParam(name = "authType", value = "权限认证类型", paramType = "query")
    })
    public Result<Page<SysResourceVo>> getResourcePage(
            @RequestParam(value = "current", defaultValue = "1", required = false) Long current,
            @RequestParam(value = "size", defaultValue = "10", required = false) Long size,
            @RequestParam(required = false) String resourceName,
            @RequestParam(value = "method", required = false) String method,
            @RequestParam(value = "authType", required = false) Integer authType) {
        return Result.success(sysResourceService.getResourcePage(current, size, resourceName, method, authType));
    }


    /**
     * 查询所有资源
     *
     * @return
     */
    @RequestMapping(value = "/getResourceList", method = RequestMethod.GET)
    @ApiOperation("查询所有资源列表")
    public Result<List<SysResourceVo>> getResourceList() {
        List<SysResource> list = sysResourceService.list();
        List<SysResourceVo> vos = BeanDtoVoUtils.listVo(list, SysResourceVo.class);

        return Result.success(vos);
    }


    /**
     * 刷新资源
     *
     * @return
     */
    @RequestMapping(value = "/refresh", method = RequestMethod.PUT)
    @ApiOperation("刷新资源")
    public Result refreshResource() {
        scanMappings.doScan();
        return Result.success("刷新成功");
    }


    /**
     * 查询用户的资源
     *
     * @param token
     * @return
     */
    @RequestMapping(value = "/getUserResources", method = RequestMethod.GET)
    @ApiOperation("查询用户的资源")
    public Result<List<SysResourceVo>> getUserResources(
            @RequestHeader(value = "Authorization",required = false) String token) {
        SysUserDetailsVo user = iSysUserService.getUserByToken(token);
        if (null == user) {
            throw new ErrorException(ResultType.USER_IS_NOT_LOGIN);
        }
        return Result.success(sysResourceService.getUserResources(user.getId()));
    }
}
