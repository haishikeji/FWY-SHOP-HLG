package com.px.business.app.appuser.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.px.business.app.appuser.entity.TbCouponRecords;
import com.px.business.app.appuser.service.TbCouponRecordsService;
import com.px.business.app.appuser.vo.PageDto;

import com.px.common.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/tb-coupon-records")
@Api(tags = "用户优惠卷领取记录表")
public class TbCouponRecordsController {

    @Autowired
    private TbCouponRecordsService tbCouponRecordsService;

    @ApiOperation("领取优惠卷")
    @PostMapping("/receive-coupon")
    @ApiImplicitParam(name = "couponId",value = "优惠券id")
    public Result receiveCoupon(@RequestParam String couponId,
                                         @RequestHeader(value = "Authorization",required = false) String token){
        return tbCouponRecordsService.receiveCoupon(couponId,token);
    }

    @ApiOperation("查询我的优惠卷")
    @GetMapping("/receive-records")
    public Result<Page<TbCouponRecords>> receiveRecords(@ModelAttribute @Validated PageDto pageVo, @RequestHeader(value = "Authorization",required = false) String token){
        Page<TbCouponRecords> page = tbCouponRecordsService.receiveRecords(pageVo,token);
        return Result.success(page);
    }
}
