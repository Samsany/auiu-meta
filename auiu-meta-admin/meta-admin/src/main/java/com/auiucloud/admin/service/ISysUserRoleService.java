package com.auiucloud.admin.service;

import com.auiucloud.admin.domain.SysUserRole;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @author dries
 * @description 针对表【sys_user_role(用户角色关联表)】的数据库操作Service
 * @createDate 2022-05-31 14:26:11
 */
public interface ISysUserRoleService extends IService<SysUserRole> {


    /**
     * 根据用户ID查询用户角色关联列表
     *
     * @param userId 用户ID
     * @return List<SysUserRole>
     */
    List<SysUserRole> getSysUserRoleListByUserId(Long userId);

    /**
     * 根据用户ID查询用户角色编码
     *
     * @param userId 用户ID
     * @return List<String>
     */
    List<String> getRoleCodeListByUserId(Long userId);

}
