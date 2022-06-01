package com.auiucloud.admin.service.impl;

import com.auiucloud.admin.domain.SysRole;
import com.auiucloud.admin.mapper.SysRoleMapper;
import com.auiucloud.admin.service.ISysRoleService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
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
    public List<SysRole> getRoleListByIds(Set<Long> roleIds) {
        LambdaQueryWrapper<SysRole> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.in(SysRole::getId, roleIds);
        return this.list(queryWrapper);
    }

}




