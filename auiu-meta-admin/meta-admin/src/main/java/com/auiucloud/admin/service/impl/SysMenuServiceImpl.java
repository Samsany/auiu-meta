package com.auiucloud.admin.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.auiucloud.admin.domain.SysMenu;
import com.auiucloud.admin.domain.SysUserRole;
import com.auiucloud.admin.dto.SysMenuDto;
import com.auiucloud.admin.mapper.SysMenuMapper;
import com.auiucloud.admin.service.ISysMenuService;
import com.auiucloud.admin.service.ISysUserRoleService;
import com.auiucloud.core.common.constant.CommonConstant;
import com.auiucloud.core.common.utils.SecurityUtil;
import com.auiucloud.core.database.model.Search;
import com.auiucloud.core.database.utils.PageUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
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
        // 超级管理员特殊处理
        Long userId = SecurityUtil.getUserId();
        if (userId.equals(CommonConstant.NODE_ONE_ID)) {
            LambdaQueryWrapper<SysMenu> queryWrapper = Wrappers.lambdaQuery();
            queryWrapper.eq(SysMenu::getStatus, CommonConstant.STATUS_NORMAL_VALUE);
            queryWrapper.orderByAsc(SysMenu::getSort);
            return this.list(queryWrapper);
        }

        List<SysUserRole> sysUserRoles = sysUserRoleService.getSysUserRoleListByUserId(userId);
        List<Long> roleIds = Optional.ofNullable(sysUserRoles).orElse(Collections.emptyList()).stream().map(SysUserRole::getRoleId).collect(Collectors.toList());
        return this.baseMapper.routes(roleIds);
    }

    @Override
    public PageUtils listPage(Search search) {
        LambdaQueryWrapper<SysMenu> queryWrapper = new LambdaQueryWrapper<>();
        if (StrUtil.isNotBlank(search.getKeyword())) {
            queryWrapper.and(i -> i
                    .or().like(SysMenu::getTitle, search.getKeyword())
                    .or().like(SysMenu::getName, search.getKeyword()));
        }
        if (ObjectUtil.isNotNull(search.getStatus())) {
            queryWrapper.eq(SysMenu::getStatus, search.getStatus());
        }
        queryWrapper.orderByAsc(SysMenu::getSort);
        return new PageUtils(this.page(PageUtils.getPage(search), queryWrapper));
    }

    @Override
    public List<SysMenu> treeList(Search search) {
        LambdaQueryWrapper<SysMenu> queryWrapper = Wrappers.lambdaQuery();
        if (StrUtil.isNotBlank(search.getKeyword())) {
            queryWrapper.and(i -> i
                    .or().like(SysMenu::getTitle, search.getKeyword())
                    .or().like(SysMenu::getName, search.getKeyword()));
        }
        if (ObjectUtil.isNotNull(search.getStatus())) {
            queryWrapper.eq(SysMenu::getStatus, search.getStatus());
        }
        queryWrapper.orderByAsc(SysMenu::getSort).orderByDesc(SysMenu::getCreateTime);
        return this.list(queryWrapper);
    }

    @Override
    public boolean createMenu(SysMenuDto menuDto) {
        SysMenu sysMenu = new SysMenu();
        BeanUtils.copyProperties(menuDto, sysMenu);
        return this.save(sysMenu);
    }

    @Override
    public boolean updateMenuById(SysMenuDto menuDto) {
        SysMenu sysMenu = new SysMenu();
        BeanUtils.copyProperties(menuDto, sysMenu);
        return this.updateById(sysMenu);
    }
}
