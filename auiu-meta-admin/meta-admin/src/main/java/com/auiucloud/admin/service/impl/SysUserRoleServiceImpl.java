package com.auiucloud.admin.service.impl;

import com.auiucloud.admin.domain.SysRole;
import com.auiucloud.admin.domain.SysUserRole;
import com.auiucloud.admin.mapper.SysUserRoleMapper;
import com.auiucloud.admin.service.ISysRoleService;
import com.auiucloud.admin.service.ISysUserRoleService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author dries
 * @description 针对表【sys_user_role(用户角色关联表)】的数据库操作Service实现
 * @createDate 2022-05-31 14:26:11
 */
@Service
@RequiredArgsConstructor
public class SysUserRoleServiceImpl extends ServiceImpl<SysUserRoleMapper, SysUserRole> implements ISysUserRoleService {

    private final ISysRoleService sysRoleService;

    @Override
    public List<SysUserRole> getSysUserRoleListByUserId(Long userId) {
        LambdaQueryWrapper<SysUserRole> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(SysUserRole::getUserId, userId);
        // 查询用户关联的所有角色ID
        return this.list(queryWrapper);
    }

    @Override
    public List<String> getRoleCodeListByUserId(Long userId) {
        List<SysUserRole> roleListByUserId = this.getSysUserRoleListByUserId(userId);
        Set<Long> roleIds = Optional.ofNullable(roleListByUserId).orElse(Collections.emptyList())
                .stream().map(SysUserRole::getRoleId).collect(Collectors.toSet());
        List<SysRole> sysRoles = sysRoleService.getRoleListByIds(roleIds);
        return Optional.ofNullable(sysRoles).orElse(Collections.emptyList())
                .stream().map(SysRole::getRoleCode).collect(Collectors.toList());
    }


}




