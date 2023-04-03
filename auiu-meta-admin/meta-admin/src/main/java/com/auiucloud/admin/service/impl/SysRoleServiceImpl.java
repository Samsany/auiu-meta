package com.auiucloud.admin.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.csp.sentinel.util.StringUtil;
import com.auiucloud.admin.domain.SysRole;
import com.auiucloud.admin.domain.SysRoleMenu;
import com.auiucloud.admin.dto.SysRoleMenuDTO;
import com.auiucloud.core.common.model.dto.UpdateStatusDTO;
import com.auiucloud.admin.mapper.SysRoleMapper;
import com.auiucloud.admin.service.ISysRoleMenuService;
import com.auiucloud.admin.service.ISysRoleService;
import com.auiucloud.admin.vo.SysRoleVO;
import com.auiucloud.core.database.model.Search;
import com.auiucloud.core.database.utils.PageUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author dries
 * @description 针对表【sys_role(角色表)】的数据库操作Service实现
 * @createDate 2022-05-31 14:26:03
 */
@Service
@RequiredArgsConstructor
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements ISysRoleService {

    private final ISysRoleMenuService sysRoleMenuService;

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
        return Optional.ofNullable(this.list(queryWrapper)).orElse(Collections.emptyList());
    }

    @Override
    public List<SysRole> getRoleListByIds(List<Long> roleIds) {
        if (CollUtil.isNotEmpty(roleIds)) {
            LambdaQueryWrapper<SysRole> queryWrapper = Wrappers.lambdaQuery();
            queryWrapper.in(SysRole::getId, roleIds);
            return Optional.ofNullable(this.list(queryWrapper)).orElse(Collections.emptyList());
        }

        return Collections.emptyList();
    }

    @Override
    public List<SysRoleVO> getSysRoleVOListByIds(List<Long> roleIds) {
        if (CollUtil.isNotEmpty(roleIds)) {
            LambdaQueryWrapper<SysRole> queryWrapper = Wrappers.lambdaQuery();
            queryWrapper.in(SysRole::getId, roleIds);
            return Optional.ofNullable(this.list(queryWrapper)).orElse(Collections.emptyList()).parallelStream()
                    .map(it -> {
                        SysRoleVO sysRoleVO = new SysRoleVO();
                        BeanUtils.copyProperties(it, sysRoleVO);
                        return sysRoleVO;
                    }).toList();
        }

        return Collections.emptyList();
    }

    @Override
    public List<SysRole> getRoleIdsByRoles(List<String> roleCodeList) {
        if (CollUtil.isNotEmpty(roleCodeList)) {
            LambdaQueryWrapper<SysRole> queryWrapper = Wrappers.lambdaQuery();
            queryWrapper.in(SysRole::getRoleCode, roleCodeList);
            return Optional.ofNullable(this.list(queryWrapper)).orElse(Collections.emptyList());
        }

        return Collections.emptyList();
    }

    /**
     * 根据角色ID获取角色详情
     *
     * @param id 角色ID
     * @return SysRoleVO
     */
    @Override
    public SysRoleVO getRoleInfoById(Long id) {
        SysRole sysRole = this.getById(id);
        SysRoleVO sysRoleVO = new SysRoleVO();
        BeanUtils.copyProperties(sysRole, sysRoleVO);

        if (ObjectUtil.isNotNull(sysRole)) {
            // 查询角色关联的菜单
            List<SysRoleMenu> rolePermissions = sysRoleMenuService.getRoleMenusByRoleId(sysRole.getId());
            Set<Long> menuIds = Optional.ofNullable(rolePermissions).orElse(new ArrayList<>())
                    .parallelStream().map(SysRoleMenu::getMenuId).collect(Collectors.toSet());
            sysRoleVO.setMenuIds(menuIds);
        }

        return sysRoleVO;
    }

    @Transactional
    @Override
    public boolean saveRole(SysRoleVO sysRole) {
        SysRole role = new SysRole();
        BeanUtils.copyProperties(sysRole, role);

        boolean result = this.save(role);
        if (result) {
            // 重新插入角色菜单关联关系
            SysRoleMenuDTO roleMenuDTO = SysRoleMenuDTO.builder()
                    .roleId(role.getId())
                    .menuIds(sysRole.getMenuIds())
                    .build();
            sysRoleMenuService.setRoleMenus(roleMenuDTO);
        }
        return result;
    }

    @Transactional
    @Override
    public boolean updateRoleById(SysRoleVO sysRole) {
        SysRole role = new SysRole();
        BeanUtils.copyProperties(sysRole, role);
        boolean result = this.updateById(role);
        if (result) {
            // 移除角色菜单关联关系
            sysRoleMenuService.removeRoleMenusByRoleId(sysRole.getId());
            // 重新插入角色菜单关联关系
            SysRoleMenuDTO roleMenuDTO = SysRoleMenuDTO.builder()
                    .roleId(role.getId())
                    .menuIds(sysRole.getMenuIds())
                    .build();
            sysRoleMenuService.setRoleMenus(roleMenuDTO);
        }

        return result;
    }

    @Override
    public boolean checkRoleNameUnique(SysRoleVO role) {
        LambdaQueryWrapper<SysRole> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysRole::getRoleName, role.getRoleName());
        queryWrapper.ne(role.getId() != null, SysRole::getId, role.getId());
        queryWrapper.last("limit 1");
        return this.count(queryWrapper) > 0;
    }

    @Override
    public boolean checkRoleCodeUnique(SysRoleVO role) {
        LambdaQueryWrapper<SysRole> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysRole::getRoleCode, role.getRoleCode());
        queryWrapper.ne(role.getId() != null, SysRole::getId, role.getId());
        queryWrapper.last("limit 1");
        return this.count(queryWrapper) > 0;
    }

    /**
     * 根据角色ID设置角色状态
     *
     * @param updateStatusDTO 参数
     * @return boolean
     */
    @Override
    public boolean setRoleStatus(UpdateStatusDTO updateStatusDTO) {
        LambdaUpdateWrapper<SysRole> wrapper = new LambdaUpdateWrapper<>();
        wrapper.set(SysRole::getStatus, updateStatusDTO.getStatus());
        wrapper.eq(SysRole::getId, updateStatusDTO.getId());
        return this.update(wrapper);
    }
}




