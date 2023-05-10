package com.auiucloud.admin.modules.system.service.impl;

import com.auiucloud.admin.modules.system.domain.SysPermission;
import com.auiucloud.admin.modules.system.mapper.SysPermissionMapper;
import com.auiucloud.admin.modules.system.service.ISysPermissionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @author dries
 * @description 针对表【sys_permission(系统权限表)】的数据库操作Service实现
 * @createDate 2022-05-31 14:25:57
 */
@Service
@RequiredArgsConstructor
public class SysPermissionServiceImpl extends ServiceImpl<SysPermissionMapper, SysPermission> implements ISysPermissionService {

}




