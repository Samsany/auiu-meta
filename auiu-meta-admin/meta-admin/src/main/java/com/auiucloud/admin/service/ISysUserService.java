package com.auiucloud.admin.service;

import com.auiucloud.admin.domain.SysUser;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @author dries
 * @description 针对表【sys_user(系统用户表)】的数据库操作Service
 * @createDate 2022-04-08 14:58:39
 */
public interface ISysUserService extends IService<SysUser> {

    /**
     * 根据用户名查找用户
     *
     * @param username 用户名
     * @return SysUser
     */
    SysUser getUserByUsername(String username);

}
