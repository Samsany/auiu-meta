package com.auiucloud.admin.service;

import com.auiucloud.admin.domain.SysUser;
import com.auiucloud.core.database.model.Search;
import com.auiucloud.core.database.utils.PageUtils;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @author dries
 * @description 针对表【sys_user(系统用户表)】的数据库操作Service
 * @createDate 2022-04-08 14:58:39
 */
public interface ISysUserService extends IService<SysUser> {

    /**
     * 查询系统用户列表
     *
     * @param search 搜索参数
     * @param sysUser 系统用户
     * @return List<SysUser>
     */
    List<SysUser> selectSysUserList(Search search, SysUser sysUser);

    /**
     * 查询系统用户分页列表
     *
     * @param search 搜索参数
     * @param sysUser 系统用户
     * @return PageUtils
     */
    PageUtils listPage(Search search, SysUser sysUser);

    /**
     * 根据用户名查找用户
     *
     * @param username 用户名
     * @return SysUser
     */
    SysUser getUserByUsername(String username);

    /**
     * 根据部门ID查找用户
     *
     * @param deptId 部门ID
     * @return List<SysUser>
     */
    List<SysUser> selectSysUserByDeptIdList(Long deptId);
}
