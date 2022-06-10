package com.auiucloud.admin.controller;

import com.auiucloud.admin.domain.SysMenu;
import com.auiucloud.admin.service.ISysMenuService;
import com.auiucloud.admin.utils.TreeUtil;
import com.auiucloud.core.common.api.ApiResult;
import com.auiucloud.core.common.tree.ForestNodeMerger;
import com.auiucloud.core.log.annotation.Log;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 菜单权限表 前端控制器
 *
 * @author dries
 * @createDate 2022-06-08 15-55
 */
@Slf4j
@Api(tags = "菜单管理")
@RestController
@RequiredArgsConstructor
@RequestMapping("/menu")
public class SysMenuController {

    private final ISysMenuService sysMenuService;

    @Log(value = "路由列表", exception = "菜单树请求异常")
    @ApiOperation(value = "路由列表", notes = "根据用户查询路由列表")
    @GetMapping("/routes")
    public ApiResult<?> dynamicRoutes() {
        List<SysMenu> list = sysMenuService.routes();
        return ApiResult.data(ForestNodeMerger.merge(TreeUtil.buildMenuTree(list)));
    }

}
