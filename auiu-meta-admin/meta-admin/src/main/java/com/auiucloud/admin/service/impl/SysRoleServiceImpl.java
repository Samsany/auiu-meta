package com.auiucloud.admin.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.csp.sentinel.util.StringUtil;
import com.auiucloud.admin.domain.SysRole;
import com.auiucloud.admin.mapper.SysRoleMapper;
import com.auiucloud.admin.service.ISysRoleService;
import com.auiucloud.core.database.model.Search;
import com.auiucloud.core.database.utils.PageUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

/**
 * @author dries
 * @description 针对表【sys_role(角色表)】的数据库操作Service实现
 * @createDate 2022-05-31 14:26:03
 */
@Service
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements ISysRoleService {

    @Override
    public PageUtils listPage(Search search, SysRole sysRole) {
        LambdaQueryWrapper<SysRole> queryWrapper = buildSearchParams(search, sysRole);
        return new PageUtils(this.page(PageUtils.getPage(search), queryWrapper));
    }

    @NotNull
    private static LambdaQueryWrapper<SysRole> buildSearchParams(Search search, SysRole sysRole) {
        LambdaQueryWrapper<SysRole> queryWrapper = new LambdaQueryWrapper<>();
        if (StringUtil.isNotBlank(search.getStartDate())) {
            queryWrapper.between(SysRole::getCreateTime, search.getStartDate(), search.getEndDate());
        }
        if (StringUtil.isNotBlank(search.getKeyword())) {
            queryWrapper.like(SysRole::getId, search.getKeyword());
        }
        if (StringUtil.isNotBlank(sysRole.getRoleName())) {
            queryWrapper.like(SysRole::getRoleName, sysRole.getRoleName());
        }
        if (StringUtil.isNotBlank(sysRole.getRoleCode())) {
            queryWrapper.like(SysRole::getRoleCode, sysRole.getRoleCode());
        }
        if (ObjectUtil.isNotNull(search.getStatus())) {
            queryWrapper.eq(SysRole::getStatus, search.getStatus());
        }
        queryWrapper.orderByAsc(SysRole::getSort);
        queryWrapper.orderByDesc(SysRole::getCreateTime);
        return queryWrapper;
    }

    @Override
    public List<SysRole> selectSysRoleList(Search search, SysRole sysRole) {
        LambdaQueryWrapper<SysRole> queryWrapper = buildSearchParams(search, sysRole);
        return this.list(queryWrapper);
    }

    @Override
    public List<SysRole> getRoleListByIds(Set<Long> roleIds) {
        LambdaQueryWrapper<SysRole> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.in(SysRole::getId, roleIds);
        return this.list(queryWrapper);
    }

    @Override
    public List<SysRole> getRoleIdsByRoles(List<String> roles) {
        LambdaQueryWrapper<SysRole> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.in(SysRole::getRoleCode, roles);
        return this.list(queryWrapper);
    }

    @Override
    public boolean setRoleStatus(Long id, Integer status) {
        LambdaUpdateWrapper<SysRole> wrapper = new LambdaUpdateWrapper<>();
        wrapper.set(SysRole::getStatus, status);
        wrapper.eq(SysRole::getId, id);
        return this.update(wrapper);
    }
}




