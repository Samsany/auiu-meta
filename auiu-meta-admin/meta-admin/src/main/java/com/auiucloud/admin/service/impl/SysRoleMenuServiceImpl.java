package com.auiucloud.admin.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.auiucloud.admin.domain.SysRoleMenu;
import com.auiucloud.admin.dto.SysRoleMenuDTO;
import com.auiucloud.admin.mapper.SysRoleMenuMapper;
import com.auiucloud.admin.service.ISysRoleMenuService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author dries
 * @description 针对表【sys_role_menu(角色菜单关联表)】的数据库操作Service实现
 * @createDate 2022-05-31 14:26:11
 */
@Service
public class SysRoleMenuServiceImpl extends ServiceImpl<SysRoleMenuMapper, SysRoleMenu> implements ISysRoleMenuService {

    @Override
    public List<SysRoleMenu> getRoleMenusByRoleId(Long roleId) {
        LambdaQueryWrapper<SysRoleMenu> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysRoleMenu::getRoleId, roleId);
        return this.list(queryWrapper);
    }

    @Override
    @Transactional
    public boolean setRoleMenus(SysRoleMenuDTO roleMenuDTO) {
        Long roleId = roleMenuDTO.getRoleId();
        Set<Long> menuIds = roleMenuDTO.getMenuIds();

        // 批量删除角色菜单关联关系
        // LambdaQueryWrapper<SysRoleMenu> queryWrapper = new LambdaQueryWrapper<>();
        // queryWrapper.eq(SysRoleMenu::getRoleId, roleId);
        // this.remove(queryWrapper);
        // 批量删除角色权限关联关系
        // 1.查询菜单角色列表
        List<SysRoleMenu> sysRoleMenus = this.getRoleMenusByRoleId(roleId);
        // 2.筛选出要删除的菜单ID数组
        Set<Long> delMenuIds = Optional.ofNullable(sysRoleMenus).orElse(Collections.emptyList())
                .stream()
                .filter(sysRoleMenu -> menuIds.contains(sysRoleMenu.getMenuId()))
                .map(SysRoleMenu::getMenuId)
                .collect(Collectors.toSet());
        // 3.筛选出重复的数据

        if (CollUtil.isNotEmpty(menuIds)) {
            List<SysRoleMenu> sysRoleMenuList = new ArrayList<>(menuIds.size());
            for (Long menuId : menuIds) {
                SysRoleMenu sysRoleMenu = SysRoleMenu.builder()
                        .roleId(roleId)
                        .menuId(menuId)
                        .build();
                sysRoleMenuList.add(sysRoleMenu);
            }
            // 批量插入
            return this.saveBatch(sysRoleMenuList);
        }

        return true;
    }

    @Override
    public boolean checkMenuExistRole(Long menuId) {
        LambdaQueryWrapper<SysRoleMenu> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysRoleMenu::getMenuId, menuId);
        long count = this.count(queryWrapper);
        return count > 0;
    }
}




