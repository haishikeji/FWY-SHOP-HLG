package com.px.business.manage.commodity.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.px.business.manage.commodity.dto.CommodityBrandDto;
import com.px.business.manage.commodity.entity.Commodity;
import com.px.business.manage.commodity.entity.CommodityBrand;
import com.px.business.manage.commodity.mapper.CommodityBrandMapper;
import com.px.business.manage.commodity.mapper.CommodityMapper;
import com.px.business.manage.commodity.service.ICommodityBrandService;
import com.px.business.manage.commodity.vo.CommodityBrandVo;
import com.px.common.exception.ErrorException;
import com.px.common.result.Result;
import com.px.common.result.ResultType;
import com.px.common.utils.BeanDtoVoUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @@author 品讯科技
 * @Description: 商品品牌表 服务实现类
 * @Date: 2021-03-22
 **/
@Service
public class CommodityBrandServiceImpl extends ServiceImpl<CommodityBrandMapper,CommodityBrand> implements ICommodityBrandService {

    @Autowired
    private CommodityBrandMapper commodityBrandMapper;

    @Autowired
    private CommodityMapper commodityMapper;

    @Override
    public Result addCommodityBrand(CommodityBrandDto dto) {
        List<CommodityBrand> commodityBrands = baseMapper.selectList(new LambdaQueryWrapper<CommodityBrand>()
                .select(CommodityBrand::getId, CommodityBrand::getBrandName)
        );
        List<String> collect = commodityBrands.stream().map(CommodityBrand::getBrandName).collect(Collectors.toList());
        if (collect.contains(dto.getBrandName())){
            throw new ErrorException(ResultType.BRAND_IS_EXIST);
        }
        CommodityBrand commodityBrand = BeanDtoVoUtils.convert(dto,CommodityBrand.class);
        boolean result = commodityBrandMapper.insert(commodityBrand) > 0;
        return result ? Result.success() : Result.failure(ResultType.SYS_FAILURE_INSERT);
    }

    @Override
    public Result modifyCommodityBrand(CommodityBrandDto dto) {
        if (StringUtils.isBlank(dto.getId())) {
            throw new ErrorException(ResultType.SELECT_OPERATION_DATA);
        }
        CommodityBrand commodityBrand = commodityBrandMapper.selectById(dto.getId());
        if (commodityBrand == null) {
            throw new ErrorException(ResultType.OPERATION_DATA_IS_NULL);
        }
        CommodityBrand entity = BeanDtoVoUtils.convert(dto,CommodityBrand.class);
        boolean result = commodityBrandMapper.updateById(entity) > 0;
        return result ? Result.success() : Result.failure(ResultType.SYS_FAILURE_UPDATE);
    }

    @Override
    public Result deleteCommodityBrand(String commodityBrandId) {
        if (StringUtils.isBlank(commodityBrandId)) {
            throw new ErrorException(ResultType.SELECT_OPERATION_DATA);
        }
        CommodityBrand commodityBrand = commodityBrandMapper.selectById(commodityBrandId);
        if (commodityBrand == null) {
            return Result.success();
        }
        Integer count = commodityMapper.selectCount(new LambdaQueryWrapper<Commodity>()
                .eq(Commodity::getBrandId, commodityBrandId));
        if (count > 0) {
            throw new ErrorException(ResultType.USED_COMMODITY_BRAND_CANNOT_DELETE);
        }
        boolean result = commodityBrandMapper.deleteById(commodityBrandId) > 0;
        return result ? Result.success() : Result.failure(ResultType.SYS_FAILURE_DELETE);
    }

    @Override
    public CommodityBrandVo queryCommodityBrandInfo(String commodityBrandId) {
        if (StringUtils.isBlank(commodityBrandId)) {
            return null;
        }
        CommodityBrand commodityBrand = commodityBrandMapper.selectById(commodityBrandId);
        return BeanDtoVoUtils.convert(commodityBrand,CommodityBrandVo.class);
    }

    @Override
    public Result setUpState(String commodityBrandId, Integer state) {
        if (StringUtils.isBlank(commodityBrandId)) {
            return null;
        }
        CommodityBrand commodityBrand = commodityBrandMapper.selectById(commodityBrandId);
        if (commodityBrand == null) {
            throw new ErrorException(ResultType.OPERATION_DATA_IS_NULL);
        }
        CommodityBrand entity = new CommodityBrand();
        entity.setId(commodityBrandId);
        entity.setState(state);
        boolean result = commodityBrandMapper.updateById(entity) > 0;
        return result ? Result.success() : Result.failure(ResultType.SET_DATA_IS_NULL);
    }

    @Override
    public Page<CommodityBrandVo> queryManageCommodityBrandPage(Page<CommodityBrandVo> page, Map<String, Object> map) {
        return commodityBrandMapper.queryManageCommodityBrandPage(page,map);
    }

    @Override
    public List<CommodityBrandVo> queryCommodityBrandList(String brandName) {
        return commodityBrandMapper.queryCommodityBrandList(brandName);
    }

    @Override
    public List<CommodityBrandDto> getBrandList(Set<String> ids) {
        if(CollectionUtils.isEmpty(ids)){
            return new ArrayList<>(0);
        }
        List<CommodityBrand> commodityBrands = baseMapper.selectList(new LambdaQueryWrapper<CommodityBrand>()
                .in(CommodityBrand::getId, ids)
                .eq(CommodityBrand::getState, 0));
        if(CollectionUtils.isEmpty(commodityBrands)){
            return new ArrayList<>(0);
        }
        return BeanDtoVoUtils.listVo(commodityBrands,CommodityBrandDto.class);
    }

    @Override
    public CommodityBrandDto getBrand(String id) {
        if(StringUtils.isBlank(id)){
            return new CommodityBrandDto();
        }
        CommodityBrand entity = baseMapper.selectOne(new LambdaQueryWrapper<CommodityBrand>()
                .eq(CommodityBrand::getId, id)
                .eq(CommodityBrand::getState, 0));
        if(Objects.isNull(entity)){
            return new CommodityBrandDto();
        }
        return BeanDtoVoUtils.convert(entity,CommodityBrandDto.class);
    }
}

