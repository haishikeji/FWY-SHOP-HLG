package com.px.business.app.other.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.px.business.manage.appuser.service.IAppletUserService;
import com.px.business.manage.appuser.vo.AppletUserInfoVo;
import com.px.business.manage.other.entity.IntegralRecord;
import com.px.business.manage.other.service.IIntegralRecordService;
import com.px.business.manage.other.vo.IntegralRecordVo;
import com.px.common.exception.ErrorException;
import com.px.common.result.Result;
import com.px.common.result.ResultType;
import com.px.common.utils.BeanDtoVoUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @@author 品讯科技
 * @Description: 积分记录 前端控制器
 * @Date: 2021-03-15
 **/
@RestController
@RequestMapping("/app/integralRecord")
@Api(value = "/app/integralRecord", tags = "积分记录")
public class AppIntegralRecordController {

    @Autowired
    private IIntegralRecordService integralRecordService;

    @Autowired
    private IAppletUserService iAppletUserService;

    /**
     * 查询积分记录
     *
     * @param size
     * @param current
     * @param startTime
     * @param endTime
     * @return
     */
    @RequestMapping(value = "/getPage", method = RequestMethod.GET)
    @ApiOperation("查询积分记录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "size", value = "分页条数", required = true, defaultValue = "10", paramType = "query"),
            @ApiImplicitParam(name = "current", value = "页码", required = true, defaultValue = "1", paramType = "query"),
            @ApiImplicitParam(name = "startTime", value = "开始时间", required = false, paramType = "query"),
            @ApiImplicitParam(name = "endTime", value = "结束时间", required = false, paramType = "query"),
    })
    public Result<IPage<IntegralRecordVo>> getPage(
            @RequestHeader(value = "Authorization",required = false) String token,
            @RequestParam(value = "size", defaultValue = "10") Integer size,
            @RequestParam(value = "current", defaultValue = "1") Integer current,
            @RequestParam(required = false) String startTime,
            @RequestParam(required = false) String endTime
    ) {
        AppletUserInfoVo user = iAppletUserService.getAppletUserByToken(token);
        if (null == user) {
            throw new ErrorException(ResultType.USER_IS_NOT_LOGIN);
        }

        Page<IntegralRecord> page = new Page<>(current, size);
        integralRecordService.page(page,
                new LambdaQueryWrapper<IntegralRecord>()
                        .eq(IntegralRecord::getUserId, user.getId())
                        .ge(!StringUtils.isBlank(startTime), IntegralRecord::getCreateTime, startTime)
                        .le(!StringUtils.isBlank(endTime), IntegralRecord::getCreateTime, endTime)
                        .orderByDesc(IntegralRecord::getCreateTime)

        );
        IPage<IntegralRecordVo> pageVo = BeanDtoVoUtils.pageVo(page, IntegralRecordVo.class);
        return Result.success(pageVo);
    }


}
