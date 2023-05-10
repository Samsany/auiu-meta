package com.auiucloud.admin.modules.system.service.impl;

import com.auiucloud.admin.modules.system.domain.SysRolePermission;
import com.auiucloud.admin.modules.system.mapper.SysRolePermissionMapper;
import com.auiucloud.admin.modules.system.service.ISysRolePermissionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @author dries
 * @description 针对表【sys_role_permission(角色权限关联表)】的数据库操作Service实现
 * @createDate 2022-05-31 14:26:11
 */
@Service
@RequiredArgsConstructor
public class SysRolePermissionServiceImpl extends ServiceImpl<SysRolePermissionMapper, SysRolePermission> implements ISysRolePermissionService {

}




