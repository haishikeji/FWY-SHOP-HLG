package com.px.business.manage.other.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.px.business.manage.other.dto.IntegralRulesDto;
import com.px.business.manage.other.entity.IntegralRules;
import com.px.business.manage.other.mapper.IntegralRulesMapper;
import com.px.business.manage.other.service.IIntegralRulesService;
import com.px.common.result.Result;
import com.px.common.utils.BeanDtoVoUtils;
import com.px.sysuser.vo.SysUserDetailsVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @@author 品讯科技
 * @Description: 积分规则 服务实现类
 * @Date: 2021-03-12
 **/
@Service
public class IntegralRulesServiceImpl extends ServiceImpl<IntegralRulesMapper,
        IntegralRules> implements IIntegralRulesService {

    @Autowired
    private IntegralRulesMapper integralRulesMapper;


    /**
     * 增加积分规则
     *
     * @param user
     * @param dtoList
     * @return
     */
    @Override
    public Result add(SysUserDetailsVo user, List<IntegralRulesDto> dtoList) {

        List<IntegralRules> list = new ArrayList<>();
        dtoList.forEach(dto -> {
            IntegralRules convert = BeanDtoVoUtils.convert(dto, IntegralRules.class);
            if (!StringUtils.isBlank(dto.getRulesId())) {
                convert.setId(dto.getRulesId());
            }
            list.add(convert);
        });
        saveOrUpdateBatch(list);
        return Result.success();

    }
}

