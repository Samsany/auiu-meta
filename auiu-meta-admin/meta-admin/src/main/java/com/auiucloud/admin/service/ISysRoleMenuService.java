package com.auiucloud.admin.service;

import com.auiucloud.admin.domain.SysRoleMenu;
import com.auiucloud.admin.dto.SysRoleMenuDTO;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @author dries
 * @description 针对表【sys_role_menu(角色菜单关联表)】的数据库操作Service
 * @createDate 2022-05-31 14:26:11
 */
public interface ISysRoleMenuService extends IService<SysRoleMenu> {

    /**
     * 根据角色ID查询菜单列表
     *
     * @param roleId 角色ID
     * @return List<SysRoleMenu>
     */
    List<SysRoleMenu> getRoleMenusByRoleId(Long roleId);

    /**
     * 设置菜单权限
     *
     * @param roleMenuDTO 角色菜单传输对象
     * @return boolean
     */
    boolean setRoleMenus(SysRoleMenuDTO roleMenuDTO);

    /**
     * 根据菜单ID查询是否分配角色
     *
     * @param menuId 菜单ID
     * @return boolean
     */
    boolean checkMenuExistRole(Long menuId);
}
