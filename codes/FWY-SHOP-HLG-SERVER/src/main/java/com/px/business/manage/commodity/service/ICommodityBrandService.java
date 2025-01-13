package com.px.business.manage.commodity.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.px.business.manage.commodity.dto.CommodityBrandDto;
import com.px.business.manage.commodity.entity.CommodityBrand;
import com.px.business.manage.commodity.vo.CommodityBrandVo;
import com.px.common.result.Result;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @@author 品讯科技
 * @Description: 商品品牌表 服务类
 * @Date: 2021-03-22
 **/
public interface ICommodityBrandService extends IService<CommodityBrand> {

    /**
     * 添加品牌管理
     * @param dto
     * @return
     */
    Result addCommodityBrand(CommodityBrandDto dto);

    /**
     * 修改品牌管理
     * @param dto
     * @return
     */
    Result modifyCommodityBrand(CommodityBrandDto dto);

    /**
     * 删除品牌
     * @param commodityBrandId
     * @return
     */
    Result deleteCommodityBrand(String commodityBrandId);

    /**
     * 查看品牌详情
     * @param commodityBrandId
     * @return
     */
    CommodityBrandVo queryCommodityBrandInfo(String commodityBrandId);

    /**
     * 设置状态
     * @param commodityBrandId
     * @param state
     * @return
     */
    Result setUpState(String commodityBrandId,Integer state);

    /**
     * 分页查询品牌管理
     * @param page
     * @param map
     * @return
     */
    Page<CommodityBrandVo> queryManageCommodityBrandPage(Page<CommodityBrandVo> page, Map<String,Object> map);

    /**
     * 查询品牌列表
     * @param brandName
     * @return
     */
    List<CommodityBrandVo> queryCommodityBrandList(String brandName);

    /**
     * 获取品牌列表
     *
     * @param ids ids
     * @return list
     */
    List<CommodityBrandDto> getBrandList(Set<String> ids);

    /**
     * 获取品牌
     *
     * @param id id
     * @return dto
     */
    CommodityBrandDto getBrand(String id);
}
