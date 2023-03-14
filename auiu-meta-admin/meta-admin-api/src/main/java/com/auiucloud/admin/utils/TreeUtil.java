package com.auiucloud.admin.utils;

import cn.hutool.core.util.StrUtil;
import com.auiucloud.admin.domain.SysMenu;
import com.auiucloud.admin.enums.MenuOpenTypeEnum;
import com.auiucloud.admin.vo.RouteVO;
import com.auiucloud.admin.vo.SysMenuTreeVO;
import com.auiucloud.core.common.constant.CommonConstant;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
            try {
                RouteVO routeVo = new RouteVO();
                BeanUtils.copyProperties(sysMenu, routeVo);

                RouteVO.Meta meta = new RouteVO.Meta();
                meta.setTitle(sysMenu.getTitle());

                if (StrUtil.isNotBlank(sysMenu.getIcon())) {
                    meta.setIcon(sysMenu.getIcon());
                }
                if (sysMenu.getHidden() == CommonConstant.STATUS_NORMAL_VALUE) {
                    meta.setHidden(null);
                }
                if (sysMenu.getAffix() == CommonConstant.STATUS_NORMAL_VALUE) {
                    meta.setAffix(null);
                }
                if (sysMenu.getKeepalive() == CommonConstant.STATUS_NORMAL_VALUE) {
                    meta.setNoCache(null);
                }
                if (sysMenu.getHideHeader() == CommonConstant.STATUS_NORMAL_VALUE) {
                    meta.setBreadcrumb(null);
                }
                if (sysMenu.getRequireAuth() == CommonConstant.STATUS_NORMAL_VALUE) {
                    meta.setRequireAuth(null);
                }

                meta.setQueryParams(sysMenu.getQueryParams());
                meta.setSort(sysMenu.getSort());

                // 0-目录 1-菜单 2-按钮
                if (sysMenu.getType() == CommonConstant.ROOT_NODE_ID.intValue()) {
                    routeVo.setComponent(StrUtil.isBlank(sysMenu.getComponent()) ? "Layout" : sysMenu.getComponent());
                    routeVo.setRedirect("noRedirect");
                    if (sysMenu.getAlwaysShow() == CommonConstant.STATUS_DISABLE_VALUE) {
                        meta.setAlwaysShow(null);
                    }
                }
                if (Objects.equals(sysMenu.getOpenType(), MenuOpenTypeEnum.IFRAME.getValue())) { // 当打开方式为内链时
                    meta.setFrameSrc(sysMenu.getTarget());
                } else if (Objects.equals(sysMenu.getOpenType(), MenuOpenTypeEnum.LINK.getValue())) { // 当打开方式为内链时
                    routeVo.setPath(sysMenu.getTarget());
                }

                routeVo.setMeta(meta);
                trees.add(routeVo);
            } catch (Exception ignored) {
                // Exception
            }
        });
        return trees;
    }

    private static List<SysMenuTreeVO> buildMenuTree(List<SysMenu> sysMenus) {
        List<SysMenuTreeVO> list = new ArrayList<>();
        sysMenus.forEach(sysMenu -> {
            SysMenuTreeVO sysMenuVO = new SysMenuTreeVO();
            BeanUtils.copyProperties(sysMenu, sysMenuVO);
        });
        return list;
    }

}
