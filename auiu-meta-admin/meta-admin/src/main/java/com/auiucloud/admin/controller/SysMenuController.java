package com.auiucloud.admin.controller;

import com.auiucloud.admin.domain.SysMenu;
import com.auiucloud.admin.dto.SysMenuDto;
import com.auiucloud.admin.service.ISysMenuService;
import com.auiucloud.admin.utils.TreeUtil;
import com.auiucloud.admin.vo.SysMenuVO;
import com.auiucloud.core.common.api.ApiResult;
import com.auiucloud.core.common.tree.ForestNodeMerger;
import com.auiucloud.core.database.model.Search;
import com.auiucloud.core.log.annotation.Log;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;
import java.util.stream.Collectors;

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
        return ApiResult.data(ForestNodeMerger.merge(TreeUtil.buildRouteTree(list)));
    }

    @ApiOperation("菜单分页列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum", value = "当前页码", paramType = "query"),
            @ApiImplicitParam(name = "pageSize", value = "显示条数", paramType = "query"),
            @ApiImplicitParam(name = "keyword", value = "模糊查询关键词", paramType = "query"),
            @ApiImplicitParam(name = "status", value = "状态(false-禁用 true-启用)", paramType = "query"),
    })
    @GetMapping("/list")
    public ApiResult<?> list(@ApiIgnore Search search) {
        return ApiResult.data(sysMenuService.listPage(search));
    }

    @ApiOperation("菜单树列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "keyword", value = "模糊查询关键词", paramType = "query"),
            @ApiImplicitParam(name = "status", value = "状态(false-禁用 true-启用)", paramType = "query")
    })
    @GetMapping("/tree-list")
    public ApiResult<?> treeList(@ApiIgnore Search search) {
        List<SysMenu> list = sysMenuService.treeList(search);
        return ApiResult.data(ForestNodeMerger.merge(
                list.stream().map(menu -> {
                    SysMenuVO sysMenuVO = new SysMenuVO();
                    BeanUtils.copyProperties(menu, sysMenuVO);
                    return sysMenuVO;
                }).collect(Collectors.toList())
        ));
    }

    @ApiOperation("菜单详情")
    @ApiImplicitParam(name = "menuId", value = "菜单ID", paramType = "path")
    @GetMapping("/{menuId}")
    public ApiResult<?> getInfo(@PathVariable("menuId") Long menuId) {
        var menu = sysMenuService.getById(menuId);
        return ApiResult.data(menu);
    }

    @ApiOperation("新增菜单")
    @PostMapping
    public ApiResult<?> add(@RequestBody SysMenuDto menu) {
        boolean status = sysMenuService.createMenu(menu);
        return ApiResult.condition(status);
    }

    @ApiOperation("修改菜单")
    @PutMapping
    public ApiResult<?> update(@RequestBody SysMenuDto menu) {
        boolean status = sysMenuService.updateMenuById(menu);
        return ApiResult.condition(status);
    }

//    @ApiOperation("删除菜单")
//    @DeleteMapping("/delete")
//    public ApiResult<?> delete(@RequestBody Long[] ids) {
//        var menuIds = Arrays.asList(ids);
//        var delIds = menuIds.stream().filter(sysMenuService::checkHasSubMenu).collect(Collectors.toSet());
//        boolean res = sysMenuService.deleteMenuByIds(delIds);
//        return ApiResult.success(res);
//    }

}
