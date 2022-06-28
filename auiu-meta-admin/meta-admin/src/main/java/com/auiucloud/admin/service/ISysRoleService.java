package com.auiucloud.admin.service;

import com.auiucloud.admin.domain.SysRole;
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

}
