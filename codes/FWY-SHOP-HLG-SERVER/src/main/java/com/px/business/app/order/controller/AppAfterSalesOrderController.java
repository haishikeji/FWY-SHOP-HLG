package com.px.business.app.order.controller;

import com.px.business.app.order.dto.AfterSalesOrderDto;
import com.px.business.app.order.service.IAfterSalesOrderService;
import com.px.business.manage.appuser.service.IAppletUserService;
import com.px.business.manage.appuser.vo.AppletUserInfoVo;
import com.px.common.exception.ErrorException;
import com.px.common.result.Result;
import com.px.common.result.ResultType;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

/**
 * @@author 品讯科技
 * @Description: 售后订单 前端控制器
 * @Date: 2021-03-15
 **/
@RestController
@RequestMapping("/app/afterSalesOrder")
@Api(value = "/app/afterSalesOrder", tags = "售后订单")
public class AppAfterSalesOrderController {

    @Autowired
    private IAfterSalesOrderService afterSalesOrderService;

    @Autowired
    private IAppletUserService appletUserService;

    @PostMapping(value = "/generateAfterSalesOrder")
    @ApiOperation("生成售后订单")
    public Result<Map<String, String>> generateAfterSalesOrder(@RequestHeader(value = "Authorization",required = false) String token,
                                                               @RequestBody @Valid AfterSalesOrderDto dto){
        AppletUserInfoVo appletUserInfoVo = appletUserService.getAppletUserByToken(token);
        if (appletUserInfoVo == null) {
            throw new ErrorException(ResultType.USER_IS_NOT_LOGIN);
        }
        return afterSalesOrderService.generateAfterSalesOrder(appletUserInfoVo,dto);
    }

    @PutMapping(value = "/revokeAfterSalesOrder")
    @ApiOperation("撤销售后订单")
    public Result<Map<String, String>> revokeAfterSalesOrder(@RequestHeader(value = "Authorization",required = false) String token,
                                                             @RequestParam(value = "afterSalesOrderId") String afterSalesOrderId){
        AppletUserInfoVo appletUserInfoVo = appletUserService.getAppletUserByToken(token);
        if (appletUserInfoVo == null) {
            throw new ErrorException(ResultType.USER_IS_NOT_LOGIN);
        }
        return afterSalesOrderService.revokeAfterSalesOrder(afterSalesOrderId);
    }
}
