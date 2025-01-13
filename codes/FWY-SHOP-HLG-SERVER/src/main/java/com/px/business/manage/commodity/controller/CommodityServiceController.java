package com.px.business.manage.commodity.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.px.business.manage.commodity.dto.CommodityServiceDto;
import com.px.business.manage.commodity.service.ICommodityServiceService;
import com.px.business.manage.commodity.vo.CommodityServiceVo;
import com.px.common.exception.ErrorException;
import com.px.common.result.Result;
import com.px.common.result.ResultType;
import com.px.sysuser.service.ISysUserService;
import com.px.sysuser.vo.SysUserDetailsVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @@author 品讯科技
 * @Description: 商品服务表 前端控制器
 * @Date: 2021-03-23
 **/
@RestController
@RequestMapping("/commodityService")
@Api(value = "/commodityService", tags = "商品服务表")
public class CommodityServiceController{

    @Autowired
    private ISysUserService iSysUserService;

    @Autowired
    private ICommodityServiceService commodityServiceService;

    @PostMapping(value = "/addCommodityBrand")
    @ApiOperation("新增品牌分类")
    public Result addCommodityBrand(@RequestHeader(value = "Authorization",required = false) String token,
                                    @RequestBody @Valid CommodityServiceDto dto){
        SysUserDetailsVo user = iSysUserService.getUserByToken(token);
        if (null == user) {
            throw new ErrorException(ResultType.USER_IS_NOT_LOGIN);
        }
        return commodityServiceService.addCommodityService(dto);
    }

    @PutMapping(value = "/updateCommodityService")
    @ApiOperation("修改服务分类")
    public Result updateCommodityService(@RequestHeader(value = "Authorization",required = false) String token,
                                    @RequestBody @Valid CommodityServiceDto dto){
        SysUserDetailsVo user = iSysUserService.getUserByToken(token);
        if (null == user) {
            throw new ErrorException(ResultType.USER_IS_NOT_LOGIN);
        }
        return commodityServiceService.updateCommodityService(dto);
    }

    @DeleteMapping(value = "/deleteCommodityService")
    @ApiOperation("删除服务")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "commodityServiceId",value = "服务id", required = true, paramType = "query"),
    })
    public Result deleteCommodityService(@RequestHeader(value = "Authorization",required = false) String token,
                                         @RequestParam(value = "commodityServiceId") String commodityServiceId){
        SysUserDetailsVo user = iSysUserService.getUserByToken(token);
        if (null == user) {
            throw new ErrorException(ResultType.USER_IS_NOT_LOGIN);
        }
        return commodityServiceService.deleteCommodityService(commodityServiceId);
    }

    @PutMapping(value = "/setUpState")
    @ApiOperation("修改品牌状态")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "commodityServiceId",value = "服务id", required = true, paramType = "query"),
            @ApiImplicitParam(name = "state",value = "状态 0 -正常 1-禁用", required = true, paramType = "query")
    })
    public Result setUpState(@RequestHeader(value = "Authorization",required = false) String token,
                             @RequestParam(value = "commodityServiceId") String commodityServiceId,
                             @RequestParam(value = "state") Integer state){
        SysUserDetailsVo user = iSysUserService.getUserByToken(token);
        if (null == user) {
            throw new ErrorException(ResultType.USER_IS_NOT_LOGIN);
        }
        return commodityServiceService.setUpState(commodityServiceId,state);
    }

    @GetMapping(value = "/queryCommodityServiceInfo")
    @ApiOperation("查询服务详情")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "commodityServiceId",value = "服务id", required = true, paramType = "query")
    })
    public Result<CommodityServiceVo> queryCommodityServiceInfo(@RequestHeader(value = "Authorization",required = false) String token,
                                                              @RequestParam(value = "commodityServiceId") String commodityServiceId){
        SysUserDetailsVo user = iSysUserService.getUserByToken(token);
        if (null == user) {
            throw new ErrorException(ResultType.USER_IS_NOT_LOGIN);
        }
        return Result.success(commodityServiceService.queryCommodityServiceInfo(commodityServiceId));
    }

    @GetMapping(value = "/queryMangeCommodityServicePage")
    @ApiOperation("分页查询商品服务")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "serviceName",value = "服务名称", required = false, paramType = "query"),
            @ApiImplicitParam(name = "state", value = "状态 0 -正常 1-禁用", required = false, paramType = "query"),
            @ApiImplicitParam(name = "startTime", value = "开始时间", required = false, paramType = "query"),
            @ApiImplicitParam(name = "endTime", value = "结束时间", required = false, paramType = "query"),
            @ApiImplicitParam(name = "current", value = "页码", required = true, defaultValue = "1", paramType = "query"),
            @ApiImplicitParam(name = "size", value = "分页条数", required = true, defaultValue = "10", paramType = "query"),
    })
    public Result<Page<CommodityServiceVo>> queryMangeCommodityServicePage(@RequestHeader(value = "Authorization",required = false) String token,
                                                 @RequestParam(value = "serviceName",required = false) String serviceName,
                                                 @RequestParam(value = "state",required = false) Integer state,
                                                 @RequestParam(value = "startTime",required = false) String startTime,
                                                 @RequestParam(value = "endTime",required = false) String endTime,
                                                 @RequestParam(value = "current",defaultValue = "1") Integer current,
                                                 @RequestParam(value = "size",defaultValue = "10") Integer size){
        SysUserDetailsVo user = iSysUserService.getUserByToken(token);
        if (null == user) {
            throw new ErrorException(ResultType.USER_IS_NOT_LOGIN);
        }
        Page<CommodityServiceVo> page = new Page<>(current,size);
        Map<String,Object> map = new HashMap<>();
        map.put("serviceName",serviceName);
        map.put("state",state);
        map.put("startTime",startTime);
        map.put("endTime",endTime);
        map.put("current",current);
        map.put("size",size);
        return Result.success(commodityServiceService.queryMangeCommodityServicePage(page,map));
    }

    @GetMapping(value = "/queryCommodityServiceList")
    @ApiOperation("查询商品服务列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "serviceName",value = "服务名称", required = false, paramType = "query"),
    })
    public Result<List<CommodityServiceVo>> queryCommodityServiceList(@RequestHeader(value = "Authorization",required = false) String token,
                                                                      @RequestParam(value = "serviceName",required = false) String serviceName){
        SysUserDetailsVo user = iSysUserService.getUserByToken(token);
        if (null == user) {
            throw new ErrorException(ResultType.USER_IS_NOT_LOGIN);
        }
        return Result.success(commodityServiceService.queryCommodityServiceList(serviceName));
    }
}
