package com.px.sysuser.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.px.common.result.Result;
import com.px.sysuser.entity.SysRoleMenu;
import com.px.sysuser.mapper.SysRoleMenuMapper;
import com.px.sysuser.service.ISysRoleMenuService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @@author 品讯科技
 * @Description: 角色菜单关系表 服务实现类
 * @Date: 2020-05-29
 **/
@Service
@Slf4j
public class SysRoleMenuServiceImpl extends ServiceImpl<SysRoleMenuMapper, SysRoleMenu> implements ISysRoleMenuService {

    @Autowired
    private SysRoleMenuMapper sysRoleMenuMapper;

    /**
     * 角色关联菜单
     *
     * @param roleId
     * @param menuIds
     * @return
     */
    @Override
    public Result link(String roleId, List<String> menuIds) {

        //删除原来角色关联的所有资源
        QueryWrapper<SysRoleMenu> wrapper = new QueryWrapper<>();
        wrapper.eq("role_id", roleId);
        sysRoleMenuMapper.delete(wrapper);
        //重新关联资源
        List<SysRoleMenu> list = new ArrayList<>();
        menuIds.forEach(item -> {
            SysRoleMenu roleResource = new SysRoleMenu();
            roleResource.setRoleId(roleId);
            roleResource.setMenuId(item);
            list.add(roleResource);
        });
        saveBatch(list);
        return Result.success();
    }


    /**
     * 根据角色id查询菜单id列表
     *
     * @param roleId
     * @return
     */
    @Override
    public List<String> getMenuIdsByRoleId(String roleId) {
        List<SysRoleMenu> sysRoleMenus = sysRoleMenuMapper.selectList(
                new LambdaQueryWrapper<SysRoleMenu>()
                        .eq(SysRoleMenu::getRoleId, roleId)
        );
        if (sysRoleMenus == null || sysRoleMenus.size() == 0) {
            return new ArrayList<>();
        }
        List<String> collect = sysRoleMenus.stream().map(SysRoleMenu::getMenuId)
                .collect(Collectors.toList());
        return collect;
    }


    @Override
    public int  removeByRoleId(String roleId) {
        log.info("removeByRoleId ---roleId:" + roleId);
        QueryWrapper<SysRoleMenu> wrapper = new QueryWrapper();
        wrapper.eq("role_id", roleId);
        return sysRoleMenuMapper.delete(wrapper);
    }
}

