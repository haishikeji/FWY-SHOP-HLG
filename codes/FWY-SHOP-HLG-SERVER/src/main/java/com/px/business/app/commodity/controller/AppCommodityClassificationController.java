package com.px.business.app.commodity.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.px.business.manage.commodity.entity.CommodityClassification;
import com.px.business.manage.commodity.service.ICommodityClassificationService;
import com.px.business.manage.commodity.vo.CommodityClassificationListVo;
import com.px.common.result.Result;
import com.px.common.utils.BeanDtoVoUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @@author 品讯科技
 * @Description: 商品分类 前端控制器
 * @Date: 2021-02-26
 **/
@RestController
@RequestMapping("/app/commodityClassification")
@Api(value = "/app/commodityClassification", tags = "商品分类")
public class AppCommodityClassificationController {

    @Autowired
    private ICommodityClassificationService commodityClassificationService;

    /**
     * 查询商品分类列表
     *
     * @return
     */
    @RequestMapping(value = "/getList", method = RequestMethod.GET)
    @ApiOperation("查询商品分类列表")
    public Result<List<CommodityClassificationListVo>> getList() {

        List<CommodityClassification> list = commodityClassificationService.list(
                new LambdaQueryWrapper<CommodityClassification>()
                        .eq(CommodityClassification::getState, 0)
                        .select(CommodityClassification::getId, CommodityClassification::getClassificationName)
        );
        List<CommodityClassificationListVo> lisVos = BeanDtoVoUtils.listVo(list, CommodityClassificationListVo.class);
        return Result.success(lisVos);
    }

}
