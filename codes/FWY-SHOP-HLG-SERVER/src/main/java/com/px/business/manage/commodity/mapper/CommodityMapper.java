package com.px.business.manage.commodity.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.px.business.app.commodity.vo.AppCommodityJoinAGroupVo;
import com.px.business.app.commodity.vo.AppCommodityRreSaleVo;
import com.px.business.app.commodity.vo.AppCommoditySpikeVo;
import com.px.business.app.commodity.vo.AppCommodityVo;
import com.px.business.manage.commodity.entity.Commodity;
import com.px.business.manage.commodity.vo.CommodityAppPageVo;
import com.px.business.manage.commodity.vo.CommodityListVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;


/**
 * @@author 品讯科技
 * @Description: 商品表 Mapper 接口
 * @Date: 2021-03-01
 **/
@Repository
public interface CommodityMapper extends BaseMapper<Commodity> {


    /**
     * 分页查询商品
     *
     * @param page
     * @param map
     * @return
     */
    Page<CommodityListVo> getPage(@Param("page") Page<CommodityListVo> page,
                                  @Param("map") Map<String, Object> map,
                                  @Param("aboutId") String aboutId);
    Page<CommodityListVo> getPageApp(@Param("page") Page<CommodityListVo> page,
                                  @Param("map") Map<String, Object> map
                                  );

    /**
     * app查询商品详情
     *
     * @param commodityId
     * @return
     */
    AppCommodityVo getAppDetails(@Param("commodityId") String commodityId);

    /**
     * 统计店铺的商品已售数量
     *
     * @param shopId
     * @return
     */
    Integer commoditySoldCount(@Param("shopId") String shopId);


    /**
     * 分页查询商品
     *
     * @param page
     * @param map
     * @return
     */
    Page<CommodityListVo> getPageBySHopId(@Param("page") Page<CommodityListVo> page,
                                          @Param("map") Map<String, Object> map);

    /**
     * 小程序查询商品列表
     * @param page
     * @param map
     * @return
     */
    Page<CommodityAppPageVo> getRecommendCommodityPage(@Param("page") Page<CommodityAppPageVo> page,
                                                       @Param("map") Map<String, Object> map);

    /**
     * 查询商家推荐的三个商品
     * @param shopId
     * @return
     */
    List<CommodityAppPageVo> queryShopRecommendCommodity(@Param("shopId") String shopId);


    /**
     * 查询秒杀商品
     * @param page
     * @param map
     * @return
     */
    Page<AppCommoditySpikeVo> getSpikeCommodityPage(@Param("page") Page<AppCommoditySpikeVo> page,
                                                   @Param("map") Map<String, Object> map);

    /**
     * 查询预售商品
     * @param page
     * @param map
     * @return
     */
    Page<AppCommodityRreSaleVo> getPreSaleCommodityPage(@Param("page") Page<AppCommodityRreSaleVo> page,
                                                        @Param("map") Map<String, Object> map);
    /**
     * 查询拼团商品
     * @param page
     * @param map
     * @return
     */
    Page<AppCommodityJoinAGroupVo> getJoinAGroupCommodityPage(@Param("page") Page<AppCommodityJoinAGroupVo> page,
                                                              @Param("map") Map<String, Object> map);

}
