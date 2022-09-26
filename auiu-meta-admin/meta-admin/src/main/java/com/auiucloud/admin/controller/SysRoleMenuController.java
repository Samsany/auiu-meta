package com.auiucloud.admin.controller;

import com.auiucloud.admin.dto.SysRoleMenuDTO;
import com.auiucloud.admin.service.ISysRoleMenuService;
import com.auiucloud.core.common.api.ApiResult;
import com.auiucloud.core.common.controller.BaseController;
import com.auiucloud.core.log.annotation.Log;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 角色权限关联控制器
 *
 * @author Dries
 * @date 2022-09-08 18:20:47
 */
@Api(value = "角色菜单关联", tags = "角色菜单关联")
@RestController
@RequiredArgsConstructor
@RequestMapping("/roleMenu")
public class SysRoleMenuController extends BaseController {

    private final ISysRoleMenuService sysRoleMenuService;

    /**
     * 获取角色菜单权限列表
     */
    @Log(value = "获取角色菜单列表" , exception = "获取角色菜单列表请求异常")
    @GetMapping(value = "/getRoleMenus/{roleId}")
    @ApiOperation(value = "获取角色菜单列表", notes = "根据角色ID查询分配的菜单列表")
    public ApiResult<?> getRoleMenus(@PathVariable Long roleId) {
        return ApiResult.data(sysRoleMenuService.getRoleMenusByRoleId(roleId));
    }

    /**
     * 分配角色菜单权限
     */
    @Log(value = "分配角色菜单" , exception = "分配角色菜单请求异常")
    @PostMapping(value = "/setRoleMenus")
    @ApiOperation(value = "分配角色菜单", notes = "根据角色ID分配菜单")
    public ApiResult<?> setRoleMenus(@Validated @RequestBody SysRoleMenuDTO roleMenuDTO) {
        return ApiResult.condition(sysRoleMenuService.setRoleMenus(roleMenuDTO));
    }

}
