package com.px.business.manage.commodity.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.px.business.manage.commodity.dto.CommodityServiceDto;
import com.px.business.manage.commodity.entity.Commodity;
import com.px.business.manage.commodity.entity.CommodityService;
import com.px.business.manage.commodity.mapper.CommodityMapper;
import com.px.business.manage.commodity.mapper.CommodityServiceMapper;
import com.px.business.manage.commodity.service.ICommodityServiceService;
import com.px.business.manage.commodity.vo.CommodityServiceVo;
import com.px.common.exception.ErrorException;
import com.px.common.result.Result;
import com.px.common.result.ResultType;
import com.px.common.utils.BeanDtoVoUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @@author 品讯科技
 * @Description: 商品服务表 服务实现类
 * @Date: 2021-03-23
 **/
@Service
public class CommodityServiceServiceImpl extends ServiceImpl<CommodityServiceMapper,CommodityService> implements ICommodityServiceService {

    @Autowired
    private CommodityServiceMapper commodityServiceMapper;

    @Autowired
    private CommodityMapper commodityMapper;

    @Override
    public Result addCommodityService(CommodityServiceDto dto) {
        CommodityService entity = BeanDtoVoUtils.convert(dto, CommodityService.class);
        boolean result = commodityServiceMapper.insert(entity) > 0;
        return result ? Result.success() : Result.failure(ResultType.SYS_FAILURE_INSERT);
    }

    @Override
    public Result updateCommodityService(CommodityServiceDto dto) {
        if (StringUtils.isBlank(dto.getId())) {
            throw new ErrorException(ResultType.SELECT_OPERATION_DATA);
        }
        CommodityService commodityService = commodityServiceMapper.selectById(dto.getId());
        if (commodityService == null) {
            throw new ErrorException(ResultType.OPERATION_DATA_IS_NULL);
        }
        CommodityService entity = BeanDtoVoUtils.convert(dto, CommodityService.class);
        boolean result = commodityServiceMapper.updateById(entity) > 0;
        return result ? Result.success() : Result.failure(ResultType.SYS_FAILURE_UPDATE);
    }

    @Override
    public Result setUpState(String commodityServiceId, Integer state) {
        if (StringUtils.isBlank(commodityServiceId)) {
            throw new ErrorException(ResultType.SELECT_OPERATION_DATA);
        }
        CommodityService commodityService = commodityServiceMapper.selectById(commodityServiceId);
        if (commodityService == null) {
            throw new ErrorException(ResultType.OPERATION_DATA_IS_NULL);
        }
        CommodityService entity = new CommodityService();
        entity.setId(commodityServiceId);
        entity.setState(state);
        boolean result = commodityServiceMapper.updateById(entity) > 0;
        return result ? Result.success() : Result.failure(ResultType.SET_DATA_IS_NULL);
    }

    @Override
    public Result deleteCommodityService(String commodityServiceId) {
        if (StringUtils.isBlank(commodityServiceId)) {
            throw new ErrorException(ResultType.SELECT_OPERATION_DATA);
        }
        CommodityService commodityService = commodityServiceMapper.selectById(commodityServiceId);
        if (commodityService == null) {
            return Result.success();
        }
        Integer count = commodityMapper.selectCount(new LambdaQueryWrapper<Commodity>()
                .like(Commodity::getServiceId, commodityServiceId));
        if (count > 0) {
            throw new ErrorException(ResultType.USED_COMMODITY_SERVICE_CANNOT_DELETE);
        }
        boolean result = commodityServiceMapper.deleteById(commodityServiceId) > 0;
        return result ? Result.success() : Result.failure(ResultType.SYS_FAILURE_DELETE);
    }

    @Override
    public CommodityServiceVo queryCommodityServiceInfo(String commodityServiceId) {
        if (StringUtils.isBlank(commodityServiceId)) {
            return null;
        }
        CommodityService commodityService = commodityServiceMapper.selectById(commodityServiceId);
        return BeanDtoVoUtils.convert(commodityService,CommodityServiceVo.class);
    }

    @Override
    public Page<CommodityServiceVo> queryMangeCommodityServicePage(Page<CommodityServiceVo> page, Map<String, Object> map) {
        return commodityServiceMapper.queryMangeCommodityServicePage(page,map);
    }

    @Override
    public List<CommodityServiceVo> queryCommodityServiceList(String serviceName) {
        return commodityServiceMapper.queryCommodityServiceList(serviceName);
    }
}

