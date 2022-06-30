package com.auiucloud.admin.controller;

import com.auiucloud.admin.domain.SysMenu;
import com.auiucloud.admin.service.ISysMenuService;
import com.auiucloud.admin.utils.TreeUtil;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

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

    @ApiOperation("菜单分页列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum", value = "当前页码", paramType = "query", dataTypeClass = Integer.class),
            @ApiImplicitParam(name = "pageSize", value = "显示条数", paramType = "query", dataTypeClass = Integer.class),
    })
    @GetMapping("/listPage")
    public ApiResult<?> listPage(@ApiIgnore Search search) {
        return ApiResult.success(sysMenuService.listPage(search));
    }

//    @ApiOperation("菜单详情")
//    @PostMapping("/{menuId}")
//    public ApiResult<?> getInfo(@PathVariable("menuId") Long menuId) {
//        var menu = sysMenuService.getById(menuId);
//        return ApiResult.success(menu);
//    }

//    @ApiOperation("新增菜单")
//    @PostMapping("/add")
//    public ApiResult<?> add(@RequestBody MenuDto menu) {
//        boolean status = menuService.createMenu(menu);
//        return ApiResult.success(status);
//    }

//    @ApiOperation("修改菜单")
//    @PutMapping("/update")
//    public ApiResult<?> update(@RequestBody MenuDto menu) {
//        boolean status = menuService.updateMenuById(menu);
//        return ApiResult.success(status);
//    }
//
//    @ApiOperation("修改菜单显示状态")
//    @PutMapping("/update/{menuId}")
//    public ApiResult<?> updateMenuStatus(@PathVariable Long menuId, @RequestBody UpdateStatusDto status) {
//        boolean res = menuService.updateMenuStatusById(menuId, status.isStatus());
//        return ApiResult.success(res);
//    }
//
//    @ApiOperation("删除菜单")
//    @DeleteMapping("/delete")
//    public ApiResult<?> delete(@RequestBody Long[] ids) {
//        var menuIds = Arrays.asList(ids);
//        var delIds = menuIds.stream().filter(menuService::checkHasSubMenu).collect(Collectors.toSet());
//        boolean res = menuService.deleteMenuByIds(delIds);
//        return ApiResult.success(res);
//    }

}
