package com.px.business.manage.commodity.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import com.px.business.app.appuser.vo.PageDto;
import com.px.business.manage.commodity.entity.CommodityPresale;
import com.px.business.manage.commodity.service.CommodityPresaleService;
import com.px.common.result.Result;
import com.px.sysuser.service.ISysUserService;
import com.px.sysuser.vo.SysUserDetailsVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author 品讯科技
 * @since 2021-03-29
 */
@RestController
@RequestMapping("/commodity-presale")
@Api(tags = "商品预售功能")
public class CommodityPresaleController {

    @Autowired
    private CommodityPresaleService commodityPresaleService;
    @Autowired
    private ISysUserService iSysUserService;

    @ApiOperation("新增/编辑预售商品")
    @PostMapping("/addPresaleCommodity")
    public Result addPresaleCommodity(@RequestBody @Validated CommodityPresale commodityPresale,
                                      @RequestHeader(value = "Authorization",required = false) String token) {
        SysUserDetailsVo user = iSysUserService.getUserByToken(token);
        return commodityPresaleService.addPresaleCommodity(commodityPresale, user);
    }

    @ApiOperation("预售商品列表")
    @PostMapping("/selectPresaleCommodity")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "currentPage", value = "当前页", required = true),
            @ApiImplicitParam(name = "pageSize", value = "当前页大小", required = true),
            @ApiImplicitParam(name = "keywords", value = "搜索条件", required = false),
            @ApiImplicitParam(name = "commodity",value = "商品状态0-下架 1-上架",required = false)
    })
    public Result selectPresaleCommodity(@RequestBody PageDto pageVo,
                                         @RequestHeader(value = "Authorization",required = false) String token) {
        Page<CommodityPresale> page = commodityPresaleService.selectPresaleCommodity(pageVo, token);
        return Result.success(page);
    }

    @ApiOperation("商品状态修改")
    @PutMapping("/updatePresaleCommodity/{id}")
    @ApiImplicitParam(name = "id",value = "商品id")
    public Result updatePresaleCommodity(@PathVariable String id,
                                         @RequestHeader(value = "Authorization",required = false) String token){
        SysUserDetailsVo user = iSysUserService.getUserByToken(token);
        String aboutId = user.getAboutId();
        return   commodityPresaleService.updatePresaleCommodity(id,aboutId);
    }



}
