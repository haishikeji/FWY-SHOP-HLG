package com.px.business.manage.other.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.px.business.manage.other.dto.IntegralRulesDto;
import com.px.business.manage.other.entity.IntegralRules;
import com.px.common.result.Result;
import com.px.sysuser.vo.SysUserDetailsVo;

import java.util.List;

/**
 * @@author 品讯科技
 * @Description: 积分规则 服务类
 * @Date: 2021-03-12
 **/
public interface IIntegralRulesService extends IService<IntegralRules> {


    /**
     * 增加积分规则
     *
     * @param user
     * @param dtoList
     * @return
     */
    Result add(SysUserDetailsVo user, List<IntegralRulesDto> dtoList);

}
