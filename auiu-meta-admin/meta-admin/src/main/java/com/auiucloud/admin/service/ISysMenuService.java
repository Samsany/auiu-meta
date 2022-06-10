package com.auiucloud.admin.service;

import com.auiucloud.admin.domain.SysMenu;
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

}
