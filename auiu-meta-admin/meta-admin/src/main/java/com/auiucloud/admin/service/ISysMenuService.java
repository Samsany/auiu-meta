package com.auiucloud.admin.service;

import com.auiucloud.admin.domain.SysMenu;
import com.auiucloud.admin.dto.SysMenuDto;
import com.auiucloud.core.database.model.Search;
import com.auiucloud.core.database.utils.PageUtils;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @author dries
 * @description 针对表【sys_menu(系统菜单表)】的数据库操作Service
 * @createDate 2022-05-31 14:07:33
 */
public interface ISysMenuService extends IService<SysMenu> {

    /**
     * 菜单路由
     *
     * @return List<SysMenuVO>
     */
    List<SysMenu> routes();

    /**
     * 菜单列表
     *
     * @param search 查询条件
     * @return PageUtils 分页对象
     */
    PageUtils listPage(Search search);

    /**
     * 菜单树列表
     *
     * @param search 查询条件
     * @return
     */
    List<SysMenu> treeList(Search search);

    /**
     * 创建菜单
     *
     * @param menu 菜单DTO
     * @return boolean
     */
    boolean createMenu(SysMenuDto menu);

    /**
     * 根据ID更新菜单
     *
     * @param menu 菜单DTO
     * @return boolean
     */
    boolean updateMenuById(SysMenuDto menu);

    /**
     * 批量删除菜单
     *
     * @param menuIds 菜单ID数组
     * @return String
     */
    String deleteMenuByIds(Long[] menuIds);

    /**
     * 根据菜单ID查询是否存在子项
     *
     * @param menuId 菜单ID
     * @return boolean
     */
    boolean hasChildByMenuId(Long menuId);
}
