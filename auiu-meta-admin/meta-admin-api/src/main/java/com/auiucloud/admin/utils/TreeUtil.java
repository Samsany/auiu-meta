package com.auiucloud.admin.utils;

import cn.hutool.core.util.StrUtil;
import com.auiucloud.admin.domain.SysMenu;
import com.auiucloud.admin.enums.MenuTypeEnum;
import com.auiucloud.admin.vo.RouteVO;
import com.auiucloud.admin.vo.SysMenuVO;
import com.auiucloud.core.common.constant.CommonConstant;
import com.auiucloud.core.common.constant.MetaConstant;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 树型工具类
 *
 * @author dries
 * @createDate 2022-06-08 16-27
 */
public class TreeUtil {

    public static List<RouteVO> buildRouteTree(List<SysMenu> sysMenus) {
        List<RouteVO> trees = new ArrayList<>();
        sysMenus.forEach(sysMenu -> {
            RouteVO routeVo = new RouteVO();
            BeanUtils.copyProperties(sysMenu, routeVo);

            if (sysMenu.getHidden() == MetaConstant.STATUS_DISABLE_VALUE) {
                routeVo.setHidden(null);
            }
            routeVo.setAlwaysShow(null);
            // 当菜单类型为目录 && 顶级菜单时，则强制修改为Layout
            if (sysMenu.getParentId().equals(CommonConstant.ROOT_NODE_ID) && MenuTypeEnum.DIR.getCode().equals(sysMenu.getType())) {
                routeVo.setComponent("Layout");
                routeVo.setRedirect("noRedirect");
                if (sysMenu.getAlwaysShow() == MetaConstant.STATUS_DISABLE_VALUE) {
                    routeVo.setAlwaysShow(null);
                }
            }

            RouteVO.Meta meta = new RouteVO.Meta();
            meta.setTitle(sysMenu.getTitle());
            if (StrUtil.isNotBlank(sysMenu.getIcon())) {
                meta.setIcon(sysMenu.getIcon());
            }
            if (sysMenu.getAffix() == MetaConstant.STATUS_DISABLE_VALUE) {
                meta.setAffix(null);
            }
            if (sysMenu.getKeepAlive() == MetaConstant.STATUS_DISABLE_VALUE) {
                meta.setNoCache(null);
            }
            if (sysMenu.getHideHeader() == MetaConstant.STATUS_DISABLE_VALUE) {
                meta.setBreadcrumb(null);
            }
            if (sysMenu.getRequireAuth() == MetaConstant.STATUS_DISABLE_VALUE) {
                meta.setRequireAuth(null);
            }
            routeVo.setMeta(meta);
            trees.add(routeVo);
        });

        return trees;
    }

    private static List<SysMenuVO> buildMenuTree(List<SysMenu> sysMenus) {
        List<SysMenuVO> list = new ArrayList<>();
        sysMenus.forEach(sysMenu -> {
            SysMenuVO sysMenuVO = new SysMenuVO();
            BeanUtils.copyProperties(sysMenu, sysMenuVO);
        });
        return list;
    }

}
