package com.auiucloud.admin.service;

import com.auiucloud.admin.domain.SysRole;
import com.auiucloud.core.database.model.Search;
import com.auiucloud.core.database.utils.PageUtils;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Set;

/**
 * @author dries
 * @description 针对表【sys_role(角色表)】的数据库操作Service
 * @createDate 2022-05-31 14:26:03
 */
public interface ISysRoleService extends IService<SysRole> {

    /**
     * 查询系统角色分页列表
     *
     * @param search 搜索参数
     * @param sysRole 系统角色
     * @return PageUtils
     */
    PageUtils listPage(Search search, SysRole sysRole);

    /**
     * 查询系统角色列表
     *
     * @param search 搜索参数
     * @param sysRole 系统角色
     * @return List<SysRole>
     */
    List<SysRole> selectSysRoleList(Search search, SysRole sysRole);

    /**
     * 根据角色ID列表查询角色信息
     *
     * @param roleIds 角色Ids
     * @return List<SysRole>
     */
    List<SysRole> getRoleListByIds(Set<Long> roleIds);

    /**
     * 根据角色编码列表查询角色信息
     *
     * @param roles 角色编码
     * @return List<SysRole>
     */
    List<SysRole> getRoleIdsByRoles(List<String> roles);

    /**
     * 根据角色ID设置角色状态
     *
     * @param id 角色ID
     * @param status 角色状态
     * @return boolean
     */
    boolean setRoleStatus(Long id, Integer status);
}
