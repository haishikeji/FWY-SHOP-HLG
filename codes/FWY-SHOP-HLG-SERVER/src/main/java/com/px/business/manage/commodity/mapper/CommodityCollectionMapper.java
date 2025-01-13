package com.px.business.manage.commodity.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.px.business.manage.commodity.entity.CommodityCollection;
import com.px.business.manage.commodity.vo.CommodityListVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;


/**
 * @@author 品讯科技
 * @Description: 商品收藏表 Mapper 接口
 * @Date: 2021-03-02
 **/
@Repository
public interface CommodityCollectionMapper extends BaseMapper<CommodityCollection> {

    /**
     * 查询我的商品收藏列表
     *
     * @param page
     * @param userId
     * @return
     */
    Page<CommodityListVo> getMyCollection(@Param("page") Page<CommodityListVo> page,
                                          @Param("userId") String userId
    );



}
