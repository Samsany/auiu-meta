package com.auiucloud.admin.utils;

import com.auiucloud.admin.domain.SysMenu;
import com.auiucloud.admin.enums.MenuTypeEnum;
import com.auiucloud.admin.vo.RouteVO;
import com.auiucloud.core.common.constant.CommonConstant;
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

    public static List<RouteVO> buildMenuTree(List<SysMenu> sysMenus) {
        List<RouteVO> trees = new ArrayList<>();
        sysMenus.forEach(sysMenu -> {
            RouteVO routeVo = new RouteVO();
            BeanUtils.copyProperties(sysMenu, routeVo);
            RouteVO.Meta meta = new RouteVO.Meta();
            meta.setTitle(sysMenu.getTitle());
            meta.setIcon(sysMenu.getIcon());
            meta.setNoCache(sysMenu.isKeepAlive());
            meta.setAffix(sysMenu.isAffix());
            meta.setBreadcrumb(sysMenu.isHideHeader());
            meta.setRequireAuth(sysMenu.isRequireAuth());
            // 当菜单类型为目录 && 顶级菜单时，则强制修改为Layout
            if (sysMenu.getParentId().equals(CommonConstant.ROOT_NODE_ID) && MenuTypeEnum.DIR.getCode().equals(sysMenu.getType())) {
                routeVo.setComponent("Layout");
                routeVo.setRedirect("noRedirect");
                routeVo.setAlwaysShow(sysMenu.isAlwaysShow());
            }
            trees.add(routeVo);
        });

        return trees;
    }

}
