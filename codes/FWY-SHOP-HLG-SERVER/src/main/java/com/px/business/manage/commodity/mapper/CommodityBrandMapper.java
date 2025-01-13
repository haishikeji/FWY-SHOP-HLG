package com.px.business.manage.commodity.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.px.business.manage.commodity.vo.CommodityBrandVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import com.px.business.manage.commodity.entity.CommodityBrand;

import java.util.List;
import java.util.Map;


/**
 * @@author 品讯科技
 * @Description: 商品品牌表 Mapper 接口
 * @Date: 2021-03-22
 **/
@Repository
public interface CommodityBrandMapper extends BaseMapper<CommodityBrand> {

    /**
     * 品牌查询
     * @param page
     * @param map
     * @return
     */
    Page<CommodityBrandVo> queryManageCommodityBrandPage(@Param("page") Page<CommodityBrandVo> page,
                                                         @Param("map") Map<String,Object> map);

    /**
     * 查询品牌列表
     * @param brandName
     * @return
     */
    List<CommodityBrandVo> queryCommodityBrandList(@Param("brandName") String brandName);

}
