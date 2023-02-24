package com.auiucloud.admin.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.csp.sentinel.util.StringUtil;
import com.auiucloud.admin.domain.SysUser;
import com.auiucloud.admin.mapper.SysUserMapper;
import com.auiucloud.admin.service.ISysUserService;
import com.auiucloud.core.database.model.Search;
import com.auiucloud.core.database.utils.PageUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author dries
 * @description 针对表【sys_user(系统用户表)】的数据库操作Service实现
 * @createDate 2022-04-08 14:58:39
 */
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements ISysUserService {

    @Override
    public List<SysUser> selectSysUserList(Search search, SysUser sysUser) {
        LambdaQueryWrapper<SysUser> queryWrapper = buildSearchParams(search, sysUser);
        return this.list(queryWrapper);
    }

    @Override
    public PageUtils listPage(Search search, SysUser sysUser) {
        LambdaQueryWrapper<SysUser> queryWrapper = buildSearchParams(search, sysUser);
        return new PageUtils(this.page(PageUtils.getPage(search), queryWrapper));
    }

    /**
     * 根据用户名查询用户
     *
     * @param username 用户名
     * @return SysUser
     */
    @Override
    public SysUser getUserByUsername(String username) {
        LambdaQueryWrapper<SysUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysUser::getAccount, username);
        return this.getOne(queryWrapper);
    }

    /**
     * 组装LambdaQueryWrapper查询参数
     *
     * @param search 查询参数
     * @param sysUser 查询参数
     * @return queryWrapper
     */
    private LambdaQueryWrapper<SysUser> buildSearchParams(Search search, SysUser sysUser) {
        LambdaQueryWrapper<SysUser> queryWrapper = new LambdaQueryWrapper<>();
        if (StringUtil.isNotBlank(search.getStartDate())) {
            queryWrapper.between(SysUser::getCreateTime, search.getStartDate(), search.getEndDate());
        }
        if (StringUtil.isNotBlank(sysUser.getAccount())) {
            queryWrapper.like(SysUser::getAccount, sysUser.getAccount());
        }
        if (StringUtil.isNotBlank(sysUser.getRealName())) {
            queryWrapper.like(SysUser::getRealName, sysUser.getRealName());
        }
        if (StringUtil.isNotBlank(sysUser.getNickname())) {
            queryWrapper.like(SysUser::getNickname, sysUser.getNickname());
        }
        if (ObjectUtil.isNotNull(sysUser.getDeptId())) {
            queryWrapper.eq(SysUser::getDeptId, sysUser.getDeptId());
        }
        if (ObjectUtil.isNotNull(search.getStatus())) {
            queryWrapper.eq(SysUser::getStatus, search.getStatus());
        }
        queryWrapper.orderByDesc(SysUser::getCreateTime);

        return queryWrapper;
    }

    /**
     * 根据部门ID查找用户
     *
     * @param deptId 用户名
     * @return SysUser
     */
    @Override
    public List<SysUser> selectSysUserByDeptIdList(Long deptId) {
        LambdaQueryWrapper<SysUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysUser::getDeptId, deptId);
        return this.list(queryWrapper);
    }
}




