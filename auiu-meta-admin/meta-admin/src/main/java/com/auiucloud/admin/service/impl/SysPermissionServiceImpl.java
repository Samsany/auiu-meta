package com.auiucloud.admin.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.csp.sentinel.util.StringUtil;
import com.auiucloud.admin.domain.SysMenu;
import com.auiucloud.admin.domain.SysPermission;
import com.auiucloud.admin.domain.SysRole;
import com.auiucloud.admin.enums.MenuTypeEnum;
import com.auiucloud.admin.mapper.SysPermissionMapper;
import com.auiucloud.admin.service.ISysMenuService;
import com.auiucloud.admin.service.ISysPermissionService;
import com.auiucloud.admin.service.ISysRolePermissionService;
import com.auiucloud.admin.service.ISysRoleService;
import com.auiucloud.core.common.constant.MetaConstant;
import com.auiucloud.core.common.constant.RedisKeyConstant;
import com.auiucloud.core.database.model.Search;
import com.auiucloud.core.database.utils.PageUtils;
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
 * @description 针对表【sys_permission(系统权限表)】的数据库操作Service实现
 * @createDate 2022-05-31 14:25:57
 */
@Service
@RequiredArgsConstructor
public class SysPermissionServiceImpl extends ServiceImpl<SysPermissionMapper, SysPermission> implements ISysPermissionService {

}




