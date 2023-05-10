package com.auiucloud.admin.modules.system.service;

import com.auiucloud.admin.modules.system.domain.SysMenu;
import com.auiucloud.admin.modules.system.vo.SysMenuVO;
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
    boolean createMenu(SysMenuVO menu);

    /**
     * 根据ID更新菜单
     *
     * @param menu 菜单DTO
     * @return boolean
     */
    boolean updateMenuById(SysMenuVO menu);

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

    /**
     * 根据menuId查询所有的满足条件的菜单列表,其中type=2为按钮
     *
     * @param menuIds 菜单ID
     * @return List<String> 菜单权限列表
     */
    List<String> getSysMenuPermissionById(List<Long> menuIds);
}
