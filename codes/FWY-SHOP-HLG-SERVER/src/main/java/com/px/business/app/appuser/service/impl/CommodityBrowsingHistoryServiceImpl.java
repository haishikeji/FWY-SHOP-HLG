package com.px.business.app.appuser.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.px.business.app.appuser.entity.CommodityBrowsingHistory;
import com.px.business.app.appuser.mapper.CommodityBrowsingHistoryMapper;
import com.px.business.app.appuser.service.CommodityBrowsingHistoryService;
import com.px.business.app.appuser.vo.CommodityBrowsingHistoryVO;
import com.px.business.manage.commodity.service.ICommodityService;
import com.px.business.manage.commodity.vo.CommodityVo;
import com.px.business.manage.shop.entity.Shop;
import com.px.business.manage.shop.service.IShopService;
import com.px.common.result.Result;
import com.px.common.utils.BeanDtoVoUtils;
import com.px.common.utils.TokenUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

/**
 * 商品浏览记录
 * <p>
 *  ::本代码由[兮家小二]提供的代码生成器生成,如有问题,请手动修改 ::作者CSDN:https://blog.csdn.net/qq_41463655
 * </p>
 * @author 品讯科技
 * @email zly@hnpinxun.cn
 * @date 2024-08
 */
@Service
public class CommodityBrowsingHistoryServiceImpl extends ServiceImpl<CommodityBrowsingHistoryMapper, CommodityBrowsingHistory> implements CommodityBrowsingHistoryService {

    @Autowired
    private HttpServletRequest request;
    @Autowired
    private CommodityBrowsingHistoryMapper commodityBrowsingHistoryMapper;

    @Autowired
    private ICommodityService commodityService;

    @Autowired
    private IShopService shopService;

    @Override
    public Result<Boolean> record(String commodityId) {
        String token = request.getHeader("Authorization");
        if (StringUtils.isNotBlank(token)) {
            String userId = TokenUtil.getUserId(request);
            CommodityVo commodityVo = commodityService.getDetails(commodityId).getData();
            //获取店铺类型
            Shop shop = shopService.getOne(new LambdaQueryWrapper<Shop>()
                    .select(Shop::getShopType)
                    .eq(Shop::getId, commodityVo.getShopId()));
            //
            CommodityBrowsingHistory entity = new CommodityBrowsingHistory();
            entity.setUserId(userId);
            entity.setCommodityId(commodityVo.getId());
            entity.setCommodityName(commodityVo.getCommodityName());
            entity.setStock(commodityVo.getVoList().get(0).getStock());
            entity.setSellPrice(commodityVo.getVoList().get(0).getSellPrice());
            entity.setOriginalPrice(commodityVo.getVoList().get(0).getOriginalPrice());
            entity.setCommodityType(commodityVo.getCommodityType());
            entity.setCoverUrl(commodityVo.getCoverUrl());
            entity.setShopType(shop.getShopType());
            return Result.success(this.save(entity));
        } else {
            return Result.success(true);
        }
    }

    @Override
    public Page<CommodityBrowsingHistoryVO> selectList(Long current, Long size, String userId) {
        Page<CommodityBrowsingHistory> page = new Page<>(current, size);
        Page<CommodityBrowsingHistory> list = commodityBrowsingHistoryMapper.select(page,userId);
        Page<CommodityBrowsingHistoryVO> commodityBrowsingHistoryVOIPage =(Page<CommodityBrowsingHistoryVO>) BeanDtoVoUtils.pageVo(list, CommodityBrowsingHistoryVO.class);
        return commodityBrowsingHistoryVOIPage;
    }
}