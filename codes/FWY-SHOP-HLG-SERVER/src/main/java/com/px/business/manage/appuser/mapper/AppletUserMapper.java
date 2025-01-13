package com.px.business.manage.appuser.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.px.business.manage.appuser.vo.ManageAppletUserVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import com.px.business.manage.appuser.entity.AppletUser;

import java.util.Map;


/**
 * @@author 品讯科技
 * @Description: 小程序用户表 Mapper 接口
 * @Date: 2021-02-24
 **/
@Repository
public interface AppletUserMapper extends BaseMapper<AppletUser> {


    /**
     * 管理端分页查询小程序用户
     * @param page
     * @param map
     * @return
     */
    Page<ManageAppletUserVo> queryManageAppletUserPage(@Param("page") Page<ManageAppletUserVo> page,
                                                       @Param("map") Map<String, Object> map);
}
