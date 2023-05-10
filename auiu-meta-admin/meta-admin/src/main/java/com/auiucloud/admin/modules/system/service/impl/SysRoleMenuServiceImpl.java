package com.auiucloud.admin.modules.system.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.auiucloud.admin.modules.system.domain.SysRoleMenu;
import com.auiucloud.admin.modules.system.dto.SysRoleMenuDTO;
import com.auiucloud.admin.modules.system.mapper.SysRoleMenuMapper;
import com.auiucloud.admin.modules.system.service.ISysRoleMenuService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

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
        return Optional.ofNullable(this.list(queryWrapper)).orElse(Collections.emptyList());
    }

    /**
     * 根据角色ID查询菜单列表
     *
     * @param roleIds 角色ID
     * @return List<SysRoleMenu>
     */
    @Override
    public List<SysRoleMenu> getRoleMenusByRoleIds(List<Long> roleIds) {
        if (CollUtil.isNotEmpty(roleIds)) {
            LambdaQueryWrapper<SysRoleMenu> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.in(SysRoleMenu::getRoleId, roleIds);
            return Optional.ofNullable(this.list(queryWrapper)).orElse(Collections.emptyList());
        }

        return Collections.emptyList();
    }

    @Override
    @Transactional
    public boolean setRoleMenus(SysRoleMenuDTO roleMenuDTO) {
        Long roleId = roleMenuDTO.getRoleId();
        Set<Long> menuIds = roleMenuDTO.getMenuIds();

        if (CollUtil.isNotEmpty(menuIds)) {
            List<SysRoleMenu> sysRoleMenus = menuIds.parallelStream()
                    .map(it -> SysRoleMenu.builder()
                            .roleId(roleId)
                            .menuId(it)
                            .build())
                    .toList();
            return this.saveBatch(sysRoleMenus);
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

    /**
     * 移除角色权限
     *
     * @param roleId 角色ID
     * @return boolean
     */
    @Override
    public boolean removeRoleMenusByRoleId(Long roleId) {
        LambdaQueryWrapper<SysRoleMenu> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysRoleMenu::getRoleId, roleId);
        return this.remove(queryWrapper);
    }
}




