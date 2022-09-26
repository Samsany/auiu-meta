package com.auiucloud.admin.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.csp.sentinel.util.StringUtil;
import com.auiucloud.admin.domain.SysPermission;
import com.auiucloud.admin.mapper.SysPermissionMapper;
import com.auiucloud.admin.service.ISysPermissionService;
import com.auiucloud.core.database.model.Search;
import com.auiucloud.core.database.utils.PageUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author dries
 * @description 针对表【sys_permission(系统权限表)】的数据库操作Service实现
 * @createDate 2022-05-31 14:25:57
 */
@Service
public class SysPermissionServiceImpl extends ServiceImpl<SysPermissionMapper, SysPermission> implements ISysPermissionService {

    @Override
    public List<SysPermission> getPermissionListByMenuId(Long menuId) {
        LambdaQueryWrapper<SysPermission> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysPermission::getMenuId, menuId);
        return this.list(queryWrapper);
    }

    @Override
    public PageUtils listPage(Search search, SysPermission sysPermission) {
        return new PageUtils(this.page(PageUtils.getPage(search), buildSysPermissionSearch(search, sysPermission)));
    }

    @Override
    public List<SysPermission> selectSysPermissionList(Search search, SysPermission sysPermission) {
        return this.list(buildSysPermissionSearch(search, sysPermission));
    }

    /**
     * 组装查询参数
     *
     * @param search        查询参数
     * @param sysPermission 系统权限
     * @return LambdaQueryWrapper<SysPermission>
     */
    private LambdaQueryWrapper<SysPermission> buildSysPermissionSearch(Search search, SysPermission sysPermission) {
        LambdaQueryWrapper<SysPermission> queryWrapper = new LambdaQueryWrapper<>();
        if (StringUtil.isNotBlank(search.getStartDate())) {
            queryWrapper.between(SysPermission::getCreateTime, search.getStartDate(), search.getEndDate());
        }
        if (StringUtil.isNotBlank(search.getKeyword())) {
            queryWrapper.like(SysPermission::getId, search.getKeyword());
        }
        if (ObjectUtil.isNotNull(sysPermission.getMenuId())) {
            queryWrapper.eq(SysPermission::getMenuId, sysPermission.getMenuId());
        }
        if (StringUtil.isNotBlank(sysPermission.getModule())) {
            queryWrapper.eq(SysPermission::getModule, sysPermission.getModule());
        }
        if (StringUtil.isNotBlank(sysPermission.getName())) {
            queryWrapper.like(SysPermission::getName, sysPermission.getName());
        }
        if (StringUtil.isNotBlank(sysPermission.getMethod())) {
            queryWrapper.eq(SysPermission::getMethod, sysPermission.getMethod());
        }
        if (StringUtil.isNotBlank(sysPermission.getUrlPerm())) {
            queryWrapper.eq(SysPermission::getUrlPerm, sysPermission.getUrlPerm());
        }
        if (StringUtil.isNotBlank(sysPermission.getBtnPerm())) {
            queryWrapper.eq(SysPermission::getBtnPerm, sysPermission.getBtnPerm());
        }
        queryWrapper.orderByDesc(SysPermission::getCreateTime);
        return queryWrapper;
    }

}




