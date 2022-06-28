package com.auiucloud.admin.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.auiucloud.admin.domain.SysPermission;
import com.auiucloud.admin.domain.SysRole;
import com.auiucloud.admin.domain.SysRolePermission;
import com.auiucloud.admin.mapper.SysRolePermissionMapper;
import com.auiucloud.admin.service.ISysPermissionService;
import com.auiucloud.admin.service.ISysRolePermissionService;
import com.auiucloud.admin.service.ISysRoleService;
import com.auiucloud.core.common.constant.MetaConstant;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author dries
 * @description 针对表【sys_role_permission(角色权限关联表)】的数据库操作Service实现
 * @createDate 2022-05-31 14:26:11
 */
@Service
@RequiredArgsConstructor
public class SysRolePermissionServiceImpl extends ServiceImpl<SysRolePermissionMapper, SysRolePermission>
        implements ISysRolePermissionService {

    private final ISysPermissionService sysPermissionService;

    private final ISysRoleService sysRoleService;

    @Override
    public List<String> getPermissionListByRoles(List<String> roles) {
        if (roles.contains(MetaConstant.SUPER_ADMIN_CODE)) {
            List<SysPermission> permissions = sysPermissionService.list();
            return Optional.ofNullable(permissions).orElse(Collections.emptyList())
                    .stream().map(SysPermission::getBtnPerm)
                    .filter(StrUtil::isNotBlank)
                    .distinct()
                    .collect(Collectors.toList());
        }

        if (CollUtil.isNotEmpty(roles)) {
            // 获取角色ID列表
            List<SysRole> sysRoles = sysRoleService.getRoleIdsByRoles(roles);
            List<Long> roleIds = Optional.ofNullable(sysRoles).orElse(Collections.emptyList())
                    .stream().map(SysRole::getId)
                    .collect(Collectors.toList());
            // 获取权限ID列表
            List<Long> permIds = this.getPermissionIdsByRoleIds(roleIds);
            if (CollUtil.isNotEmpty(permIds)) {
                List<SysPermission> permissions = sysPermissionService.listByIds(permIds);
                return Optional.ofNullable(permissions).orElse(Collections.emptyList())
                        .stream().map(SysPermission::getBtnPerm)
                        .filter(StrUtil::isNotBlank)
                        .distinct()
                        .collect(Collectors.toList());
            }
        }

        return Collections.emptyList();
    }

    @Override
    public List<Long> getPermissionIdsByRoleIds(List<Long> roleIds) {
        if (CollUtil.isNotEmpty(roleIds)) {
            List<SysRolePermission> sysRolePermissions = this.list(new LambdaQueryWrapper<SysRolePermission>().in(SysRolePermission::getRoleId, roleIds));
            return Optional.ofNullable(sysRolePermissions).orElse(Collections.emptyList())
                    .stream().map(SysRolePermission::getPermId)
                    .collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

}




