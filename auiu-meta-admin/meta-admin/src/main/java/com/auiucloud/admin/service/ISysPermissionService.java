package com.auiucloud.admin.service;

import com.auiucloud.admin.domain.SysPermission;
import com.auiucloud.core.database.model.Search;
import com.auiucloud.core.database.utils.PageUtils;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @author dries
 * @description 针对表【sys_permission(系统权限表)】的数据库操作Service
 * @createDate 2022-05-31 14:25:57
 */
public interface ISysPermissionService extends IService<SysPermission> {

    /**
     * 查询系统权限分页列表
     *
     * @param search 搜索参数
     * @param sysPermission 系统权限
     * @return PageUtils
     */
    PageUtils listPage(Search search, SysPermission sysPermission);

    /**
     * 查询系统权限分页列表
     *
     * @param search 搜索参数
     * @param sysPermission 系统权限
     * @return List<SysPermission>
     */
    List<SysPermission> selectSysPermissionList(Search search, SysPermission sysPermission);

}
