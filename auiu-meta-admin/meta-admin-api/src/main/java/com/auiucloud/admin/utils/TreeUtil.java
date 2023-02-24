package com.auiucloud.admin.utils;

import cn.hutool.core.util.StrUtil;
import com.auiucloud.admin.domain.SysMenu;
import com.auiucloud.admin.vo.RouteVO;
import com.auiucloud.admin.vo.SysMenuVO;
import com.auiucloud.core.common.constant.CommonConstant;
import com.auiucloud.core.common.exception.ApiException;
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

                // 0-目录 1-菜单 2-外链
                switch (sysMenu.getType()) {
                    // 当菜单类型为目录 && 顶级菜单时，则强制修改为Layout
                    case 0:
                        routeVo.setComponent("Layout");
                        routeVo.setRedirect("noRedirect");
                        if (sysMenu.getAlwaysShow() == CommonConstant.STATUS_DISABLE_VALUE) {
                            meta.setAlwaysShow(null);
                        }
                        break;
                    case 1:
                        break;
                    // 当菜单类型为外链时，则强制修改为IFrame
                    case 2:
                        routeVo.setComponent("IFrame");
                        if (sysMenu.getIframe() == CommonConstant.STATUS_DISABLE_VALUE) {
                            meta.setFrameSrc(sysMenu.getIframeSrc());
                        }
                        break;
                    default:
                        throw new ApiException("暂不支持的菜单类型，请检查菜单配置");
                }
                // 当菜单类型为目录 && 顶级菜单时，则强制修改为Layout
                // if (sysMenu.getParentId().equals(CommonConstant.ROOT_NODE_ID)
                //         && MenuTypeEnum.DIR.getCode().equals(sysMenu.getType())) {
                //     routeVo.setComponent("Layout");
                //     routeVo.setRedirect("noRedirect");
                //     if (sysMenu.getAlwaysShow() == CommonConstant.STATUS_DISABLE_VALUE) {
                //         routeVo.setAlwaysShow(null);
                //     }
                // }

                routeVo.setMeta(meta);
                trees.add(routeVo);
            } catch (Exception ignored) {
                // Exception
            }
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
