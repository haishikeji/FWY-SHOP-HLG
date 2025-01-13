package com.px.business.manage.order.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.px.business.app.appuser.vo.PageDto;
import com.px.business.app.order.dto.OrderInfoDTO;
import com.px.business.app.order.service.OrderInfoService;
import com.px.business.manage.appuser.service.IAppletUserService;
import com.px.business.manage.appuser.vo.AppletUserInfoVo;
import com.px.common.result.Result;
import com.px.sysuser.service.ISysUserService;
import com.px.sysuser.vo.SysUserDetailsVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/manage-order-info")
@Api(tags = "后台商品订单管理")
public class OrderInfoController {

    @Autowired
    private OrderInfoService orderInfoService;
    @Autowired
    private ISysUserService iSysUserService;
    @Autowired
    private IAppletUserService iAppletUserService;


    @ApiOperation("订单详情")
    @GetMapping("/selectOrderInfo")
    public Result<OrderInfoDTO> selectOrderInfo(@RequestParam String orderInfoId) {
        OrderInfoDTO orderInfoDTO = orderInfoService.selectOrderInfo(orderInfoId);
        return Result.success(orderInfoDTO);
    }


    @ApiOperation("商家订单列表查询")
    @GetMapping("/selectOrderList")
    public Result<Page<OrderInfoDTO>> selectOrderList(@ModelAttribute PageDto pageDto, @RequestHeader(value = "Authorization", required = false) String token) {
        String shopId = "";
        //商家从管理端登录
        SysUserDetailsVo user = iSysUserService.getUserByToken(token);
        if (user != null) {
            shopId = user.getAboutId();
        } else {
            //商家从小程序端登录
            AppletUserInfoVo appletUserByToken = iAppletUserService.getAppletUserByToken(token);
            shopId = appletUserByToken.getShopId();
        }
        Page<OrderInfoDTO> page = orderInfoService.selectOrderListShop(pageDto, shopId);
        return Result.success(page);
    }

    @ApiOperation("点击发货")
    @PutMapping
    Result sendOrder(@RequestParam String id, @RequestParam(required = false) String logisticsNo) {
        return orderInfoService.sendOrder(id, logisticsNo);
    }

    @ApiOperation("修改物流号")
    @PutMapping("/updateLogisticsNoById")
    public Result updateLogisticsNoById(@RequestParam String id, @RequestParam String logisticsNo) {
        return orderInfoService.updateLogisticsNoById(id, logisticsNo);
    }
}
