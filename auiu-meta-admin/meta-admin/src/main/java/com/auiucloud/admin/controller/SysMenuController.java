package com.auiucloud.admin.controller;

import com.auiucloud.admin.domain.SysMenu;
import com.auiucloud.admin.dto.SysMenuDto;
import com.auiucloud.admin.service.ISysMenuService;
import com.auiucloud.admin.utils.TreeUtil;
import com.auiucloud.admin.vo.SysMenuVO;
import com.auiucloud.core.common.api.ApiResult;
import com.auiucloud.core.common.controller.BaseController;
import com.auiucloud.core.common.tree.ForestNodeMerger;
import com.auiucloud.core.database.model.Search;
import com.auiucloud.core.log.annotation.Log;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 菜单权限表 前端控制器
 *
 * @author dries
 * @createDate 2022-06-08 15-55
 */
@Slf4j
@Tag(name = "菜单管理")
@RestController
@RequiredArgsConstructor
@RequestMapping("/menu")
public class SysMenuController extends BaseController {

    private final ISysMenuService sysMenuService;

    @Log(value = "路由列表", exception = "菜单树请求异常")
    @Operation(summary = "路由列表", description = "根据用户查询路由列表")
    @GetMapping("/routes")
    public ApiResult<?> dynamicRoutes() {
        List<SysMenu> list = sysMenuService.routes();
        return ApiResult.data(ForestNodeMerger.merge(TreeUtil.buildRouteTree(list)));
    }

    @Operation(summary ="菜单分页列表")
    @Parameters({
            @Parameter(name = "pageNum", description = "当前页码", in = ParameterIn.QUERY),
            @Parameter(name = "pageSize", description = "显示条数", in = ParameterIn.QUERY),
            @Parameter(name = "keyword", description = "模糊查询关键词", in = ParameterIn.QUERY),
            @Parameter(name = "status", description = "状态(1-禁用 0-启用)", in = ParameterIn.QUERY),
    })
    @GetMapping("/list")
    public ApiResult<?> list(Search search) {
        return ApiResult.data(sysMenuService.listPage(search));
    }

    @Operation(summary ="菜单树列表")
    @Parameters({
            @Parameter(name = "keyword", description = "模糊查询关键词", in = ParameterIn.QUERY),
            @Parameter(name = "status", description = "状态(1-禁用 0-启用)", in = ParameterIn.QUERY)
    })
    @GetMapping("/tree-list")
    public ApiResult<?> treeList(Search search) {
        List<SysMenu> list = sysMenuService.treeList(search);
        return ApiResult.data(ForestNodeMerger.merge(
                list.stream().map(menu -> {
                    SysMenuVO sysMenuVO = new SysMenuVO();
                    BeanUtils.copyProperties(menu, sysMenuVO);
                    return sysMenuVO;
                }).collect(Collectors.toList())
        ));
    }

    @Operation(summary ="菜单详情")
    @Parameter(name = "menuId", description = "菜单ID", in = ParameterIn.PATH)
    @GetMapping("/{menuId}")
    public ApiResult<?> getInfo(@PathVariable("menuId") Long menuId) {
        var menu = sysMenuService.getById(menuId);
        return ApiResult.data(menu);
    }

    @Operation(summary ="新增菜单")
    @PostMapping
    public ApiResult<?> add(@RequestBody SysMenuDto menu) {
        boolean status = sysMenuService.createMenu(menu);
        return ApiResult.condition(status);
    }

    @Operation(summary ="修改菜单")
    @PutMapping
    public ApiResult<?> update(@RequestBody SysMenuDto menu) {
        boolean status = sysMenuService.updateMenuById(menu);
        return ApiResult.condition(status);
    }

    @Operation(summary ="删除菜单")
    @DeleteMapping("/delete")
    public ApiResult<?> delete(@RequestBody Long[] ids) {
        String msg = sysMenuService.deleteMenuByIds(ids);
        return ApiResult.success(msg);
    }

}
