package com.auiucloud.admin.service.impl;

import com.auiucloud.admin.domain.SysUser;
import com.auiucloud.admin.mapper.SysUserMapper;
import com.auiucloud.admin.service.ISysUserService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * @author dries
 * @description 针对表【sys_user(系统用户表)】的数据库操作Service实现
 * @createDate 2022-04-08 14:58:39
 */
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements ISysUserService {

    /**
     * 根据用户名查询用户
     *
     * @param username 用户名
     * @return SysUser
     */
    @Override
    public SysUser getUserByUsername(String username) {
        LambdaQueryWrapper<SysUser> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(SysUser::getAccount, username);
        return this.baseMapper.selectOne(queryWrapper);
    }

}




