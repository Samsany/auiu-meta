package com.auiucloud.admin.modules.system.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.auiucloud.admin.modules.system.domain.SysUserRole;
import com.auiucloud.admin.modules.system.dto.SysUserRoleDTO;
import com.auiucloud.admin.modules.system.mapper.SysUserRoleMapper;
import com.auiucloud.admin.modules.system.service.ISysUserRoleService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * @author dries
 * @description 针对表【sys_user_role(用户角色关联表)】的数据库操作Service实现
 * @createDate 2022-05-31 14:26:11
 */
@Service
@RequiredArgsConstructor
public class SysUserRoleServiceImpl extends ServiceImpl<SysUserRoleMapper, SysUserRole> implements ISysUserRoleService {

    @Override
    public List<SysUserRole> getSysUserRoleListByUserId(Long userId) {
        LambdaQueryWrapper<SysUserRole> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysUserRole::getUserId, userId);
        // 查询用户关联的所有角色ID
        return Optional.ofNullable(this.list(queryWrapper)).orElse(Collections.emptyList());
    }


    @Override
    public boolean batchAddSysUserRole(SysUserRoleDTO userRoleDTO) {
        Long userId = userRoleDTO.getUserId();
        List<Long> roleIds = userRoleDTO.getRoleIds();

        if (CollUtil.isNotEmpty(roleIds)) {
            List<SysUserRole> sysUserRoles = roleIds.parallelStream()
                    .map(it -> SysUserRole.builder()
                            .userId(userId)
                            .roleId(it)
                            .build())
                    .toList();
            return this.saveBatch(sysUserRoles);
        }

        return true;
    }

    @Override
    public boolean removeSysUserRoleByUserId(Long userId) {
        LambdaQueryWrapper<SysUserRole> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysUserRole::getUserId, userId);
        return this.remove(queryWrapper);
    }
}




