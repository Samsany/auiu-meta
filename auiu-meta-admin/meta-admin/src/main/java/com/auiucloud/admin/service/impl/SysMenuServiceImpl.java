package com.auiucloud.admin.service.impl;

import com.auiucloud.admin.domain.SysMenu;
import com.auiucloud.admin.domain.SysUserRole;
import com.auiucloud.admin.mapper.SysMenuMapper;
import com.auiucloud.admin.service.ISysMenuService;
import com.auiucloud.admin.service.ISysUserRoleService;
import com.auiucloud.core.common.utils.SecurityUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author dries
 * @description 针对表【sys_menu(系统菜单表)】的数据库操作Service实现
 * @createDate 2022-05-31 14:07:33
 */
@Service
@RequiredArgsConstructor
public class SysMenuServiceImpl extends ServiceImpl<SysMenuMapper, SysMenu> implements ISysMenuService {

    private final ISysUserRoleService sysUserRoleService;

    @Override
    public List<SysMenu> routes() {
        // TODO 超级管理员
        Long userId = SecurityUtil.getUserId();
        List<SysUserRole> sysUserRoles = sysUserRoleService.getSysUserRoleListByUserId(userId);
        List<Long> roleIds = Optional.ofNullable(sysUserRoles).orElse(Collections.emptyList())
                .stream().map(SysUserRole::getRoleId).collect(Collectors.toList());
        return this.baseMapper.routes(roleIds);
    }
}




