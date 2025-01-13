package com.px.business.manage.shop.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.px.business.app.shop.vo.ShopDetailVo;
import com.px.business.manage.appuser.vo.AppletUserInfoVo;
import com.px.business.manage.shop.dto.ShopDto;
import com.px.business.manage.shop.dto.UpdateShopDto;
import com.px.business.manage.shop.entity.Shop;
import com.px.business.manage.shop.vo.AppShopStatisticsVo;
import com.px.business.manage.shop.vo.ManageShopListVo;
import com.px.business.manage.shop.vo.ShopVo;
import com.px.common.result.Result;
import com.px.sysuser.vo.SysUserDetailsVo;

import java.util.Map;

/**
 * @@author 品讯科技
 * @Description: 商铺表 服务类
 * @Date: 2021-02-24
 **/
public interface IShopService extends IService<Shop> {

    /**
     * 平台新建商铺
     *
     * @param dto
     * @param userId
     * @param type   0-代表平台创建  1-代表小程序申请入驻
     * @return
     */
    Result addShop(ShopDto dto, String userId, Integer type);


    /**
     * 平台查询商户列表
     *
     * @param current
     * @param size
     * @param queryMap
     * @return
     */
    Result<Page<ManageShopListVo>> getShopPage(Long current, Long size, Map<String, Object> queryMap);

    /**
     * 修改商铺信息
     *
     * @param dto
     * @param user
     * @return
     */
    Result updateShopInfo(UpdateShopDto dto, SysUserDetailsVo user);

    /**
     * 查询商铺详情
     *
     * @param shopId
     * @param user
     * @return
     */
    ShopVo getShopDetails(String shopId, SysUserDetailsVo user);


    /**
     * 禁用/启用商铺
     *
     * @param shopId
     * @param shopState
     * @return
     */
    Result updateShopStatus(String shopId, Integer shopState);


    /**
     * 店铺设置可退货时间
     *
     * @param shopId
     * @param returnTime
     * @return
     */
    Result setReturnTime(String shopId, Integer returnTime);

    /**
     * 店铺名字是否存在
     *
     * @param shopName
     * @return
     */
    Boolean isExistedShopName(String shopName);


    /**
     * app商铺首页信息
     *
     * @param shopId
     * @return
     */
    Map<String,Object> getAppShopInfo(AppletUserInfoVo appletUserInfoVo,String shopId);


    /**
     * 小程序 商户统计
     *
     * @param shopId
     * @return
     */
    Result<AppShopStatisticsVo> appsStatistics(String shopId);

    /**
     * id查询店铺信息
     *
     * @param shopId
     * @return
     */
    Result<ShopDetailVo> findId(String shopId);


}
