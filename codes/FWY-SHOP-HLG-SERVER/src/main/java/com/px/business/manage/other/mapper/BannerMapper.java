package com.px.business.manage.other.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.px.business.manage.other.vo.BannerVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import com.px.business.manage.other.entity.Banner;

import java.util.Map;


/**
 * @@author 品讯科技
 * @Description: banner表 Mapper 接口
 * @Date: 2021-02-24
 **/
@Repository
public interface BannerMapper extends BaseMapper<Banner> {

    /**
     * 查询管理端banner列表
     * @param page
     * @param map
     * @return
     */
    Page<BannerVo> queryManageBannerPage(@Param("page") Page<BannerVo> page,
                                         @Param("map") Map<String, Object> map);
}
