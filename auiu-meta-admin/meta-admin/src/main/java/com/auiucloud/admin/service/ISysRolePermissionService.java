package com.auiucloud.admin.service;

import com.auiucloud.admin.domain.SysRolePermission;
import com.auiucloud.admin.dto.SysRolePermissionDTO;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @author dries
 * @description 针对表【sys_role_permission(角色权限关联表)】的数据库操作Service
 * @createDate 2022-05-31 14:26:11
 */
public interface ISysRolePermissionService extends IService<SysRolePermission> {

    /**
     * 根据角色编码列表查询权限信息
     *
     * @param roles 角色编码列表
     * @return List<String> 权限列表
     */
    List<String> getPermissionListByRoles(List<String> roles);

    /**
     * 根据角色ID列表查询权限信息
     *
     * @param roleIds 角色ID列表
     * @return List<String> 权限ID列表
     */
    List<Long> getPermissionIdsByRoleIds(List<Long> roleIds);

    /**
     * 根据角色ID查询权限列表
     *
     * @param roleId 角色ID
     * @return List<SysRolePermission>
     */
    List<SysRolePermission> getRolePermissionsByRoleId(Long roleId);

    /**
     * 设置角色权限
     *
     * @param rolePermissionDTO 角色权限传输对象
     * @return boolean
     */
    boolean setRolePermissionsByRoleId(SysRolePermissionDTO rolePermissionDTO);
}
