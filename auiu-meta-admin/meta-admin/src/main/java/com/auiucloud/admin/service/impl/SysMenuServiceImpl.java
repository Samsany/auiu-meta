package com.auiucloud.admin.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.auiucloud.admin.domain.SysMenu;
import com.auiucloud.admin.domain.SysUserRole;
import com.auiucloud.admin.vo.SysMenuVO;
import com.auiucloud.admin.enums.MenuTypeEnum;
import com.auiucloud.admin.mapper.SysMenuMapper;
import com.auiucloud.admin.service.ISysMenuService;
import com.auiucloud.admin.service.ISysRoleMenuService;
import com.auiucloud.admin.service.ISysUserRoleService;
import com.auiucloud.core.common.constant.CommonConstant;
import com.auiucloud.core.common.utils.SecurityUtil;
import com.auiucloud.core.database.model.Search;
import com.auiucloud.core.database.utils.PageUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
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
    private final ISysRoleMenuService roleMenuService;

    @Override
    public List<SysMenu> routes() {
        // 超级管理员特殊处理
        Long userId = SecurityUtil.getUserId();
        if (userId.equals(CommonConstant.NODE_ONE_ID)) {
            LambdaQueryWrapper<SysMenu> queryWrapper = Wrappers.lambdaQuery();
            queryWrapper.eq(SysMenu::getStatus, CommonConstant.STATUS_NORMAL_VALUE);
            queryWrapper.ne(SysMenu::getType, MenuTypeEnum.BUTTON.getCode());
            queryWrapper.orderByAsc(SysMenu::getSort);
            return Optional.ofNullable(this.list(queryWrapper)).orElse(Collections.emptyList());
        }

        List<SysUserRole> sysUserRoles = sysUserRoleService.getSysUserRoleListByUserId(userId);
        List<Long> roleIds = Optional.ofNullable(sysUserRoles).orElse(Collections.emptyList()).stream()
                .map(SysUserRole::getRoleId)
                .collect(Collectors.toList());
        return this.baseMapper.routes(roleIds);
    }

    @Override
    public PageUtils listPage(Search search) {
        LambdaQueryWrapper<SysMenu> queryWrapper = buildSearchParams(search);
        return new PageUtils(this.page(PageUtils.getPage(search), queryWrapper));
    }

    @Override
    public List<SysMenu> treeList(Search search) {
        LambdaQueryWrapper<SysMenu> queryWrapper = buildSearchParams(search);
        return Optional.ofNullable(this.list(queryWrapper)).orElse(Collections.emptyList());
    }

    @NotNull
    private static LambdaQueryWrapper<SysMenu> buildSearchParams(Search search) {
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
        return queryWrapper;
    }

    @Override
    public boolean createMenu(SysMenuVO menuDto) {
        SysMenu sysMenu = new SysMenu();
        BeanUtils.copyProperties(menuDto, sysMenu);
        return this.save(sysMenu);
    }

    @Override
    public boolean updateMenuById(SysMenuVO menuDto) {
        SysMenu sysMenu = new SysMenu();
        BeanUtils.copyProperties(menuDto, sysMenu);
        return this.updateById(sysMenu);
    }

    @Override
    public String deleteMenuByIds(Long[] menuIds) {

        int successNum = 0;
        int failureNum = 0;
        StringBuilder successMsg = new StringBuilder();
        StringBuilder failureMsg = new StringBuilder();

        for (Long menuId : menuIds) {

            if (this.hasChildByMenuId(menuId)) {
                failureNum++;
                failureMsg.append(CommonConstant.BR)
                        .append("【")
                        .append(menuId)
                        .append("】存在子菜单,不允许删除");
            } else if (roleMenuService.checkMenuExistRole(menuId)) {
                failureNum++;
                failureMsg.append(CommonConstant.BR)
                        .append("【")
                        .append(menuId)
                        .append("】菜单已分配,不允许删除");
            } else {
                boolean result = this.removeById(menuId);
                if (result) {
                    successNum++;
                    successMsg.append(CommonConstant.BR)
                            .append("【")
                            .append(menuId)
                            .append("】菜单删除成功");
                } else {
                    failureNum++;
                    failureMsg.append(CommonConstant.BR)
                            .append("【")
                            .append(menuId)
                            .append("】菜单删除失败");
                }
            }
        }

        if (failureNum > 0) {
            failureMsg.insert(0, "菜单删除处理成功，成功删除" + successNum + " 条数据，删除失败" + failureNum + " 条数据，删除错误内容如下：");
            return failureMsg.toString();
        } else {
            successMsg.insert(0, "恭喜您，选中数据已全部删除成功！共 " + successNum + " 条，数据如下：");
            return successMsg.toString();
        }
    }

    @Override
    public boolean hasChildByMenuId(Long menuId) {
        LambdaQueryWrapper<SysMenu> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysMenu::getParentId, menuId);
        long count = this.count(queryWrapper);
        return count > 0;
    }

    @Override
    public List<String> getSysMenuPermissionById(List<Long> menuIds) {
        if (CollUtil.isNotEmpty(menuIds)) {
            //3.根据menuId查询所有的满足条件的菜单列表,其中type=2为按钮
            LambdaQueryWrapper<SysMenu> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(SysMenu::getType, MenuTypeEnum.BUTTON.getCode());
            queryWrapper.in(SysMenu::getId, menuIds);
            List<SysMenu> menuList = this.list(queryWrapper);
            return Optional.ofNullable(menuList).orElse(Collections.emptyList()).parallelStream()
                    .map(SysMenu::getPermission)
                    .collect(Collectors.toList());
        }

        return Collections.emptyList();
    }

}
