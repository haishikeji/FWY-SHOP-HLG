package com.px.sysuser.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.px.sysuser.entity.SysUserRole;
import com.px.sysuser.mapper.SysUserRoleMapper;
import com.px.sysuser.service.ISysUserRoleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @@author 品讯科技
 * @Description: 系统用户角色关系表 服务实现类
 * @Date: 2020-05-26
 **/
@Service
@Slf4j
public class SysUserRoleServiceImpl extends ServiceImpl<SysUserRoleMapper, SysUserRole>
        implements ISysUserRoleService {

    @Autowired
    private SysUserRoleMapper sysUserRoleMapper;

    @Override
    public boolean linkedRoles(String userId, List<String> roleIds) {
        log.info("linkedRoles-- userId:" + userId + ",roleIds:" + roleIds);
        //删除该用户的所有角色
        QueryWrapper<SysUserRole> wrapper = new QueryWrapper<>();
        wrapper.eq("uid", userId);
        sysUserRoleMapper.delete(wrapper);
        //重新封装数据之后保存
        List<SysUserRole> list = new ArrayList<>();
        roleIds.forEach(item -> {
            SysUserRole userRole = new SysUserRole();
            userRole.setRoleId(item).setUid(userId);
            list.add(userRole);
        });
        return saveBatch(list);
    }


    /**
     * 查询用户关联角色
     *
     * @param userId
     * @return
     */
    @Override
    public List<String> queryUserRoleList(String userId) {
        List<SysUserRole> userRoleList = sysUserRoleMapper.selectList(
                new LambdaQueryWrapper<SysUserRole>()
                        .eq(SysUserRole::getUid, userId)
                        .select(SysUserRole::getRoleId)
        );
        if (userRoleList == null || userRoleList.size() == 0) {
            List<String> res = new ArrayList<>();
            return res;
        }
        List<String> result = userRoleList.stream().map(SysUserRole::getRoleId)
                .collect(Collectors.toList());
        return result;

    }

    @Override
    public int removeByRoleId(String roleId) {
        log.info("removeByRoleId-- roleId:" + roleId);
        //删除该角色的关联信息
        QueryWrapper<SysUserRole> wrapper = new QueryWrapper<>();
        wrapper.eq("role_id", roleId);
        return sysUserRoleMapper.delete(wrapper);
    }


}

