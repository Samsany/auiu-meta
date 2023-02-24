package com.auiucloud.admin.controller;

import com.auiucloud.admin.dto.SysRolePermissionDTO;
import com.auiucloud.admin.service.ISysRolePermissionService;
import com.auiucloud.core.common.api.ApiResult;
import com.auiucloud.core.common.controller.BaseController;
import com.auiucloud.core.log.annotation.Log;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 角色权限关联控制器
 *
 * @author Dries
 * @date 2022-09-08 18:20:47
 */
@Tag(name = "角色权限关联")
@RestController
@RequiredArgsConstructor
@RequestMapping("/rolePerm")
public class SysRolePermissionController extends BaseController {

    private final ISysRolePermissionService sysRolePermissionService;

    /**
     * 获取角色权限列表
     */
    @Log(value = "获取角色权限列表" , exception = "获取角色权限列表请求异常")
    @GetMapping(value = "/getRolePerms/{roleId}")
    @Operation(summary = "获取角色权限列表", description = "根据角色ID查询分配的权限列表")
    public ApiResult<?> getRolePermissions(@PathVariable Long roleId) {
        return ApiResult.data(sysRolePermissionService.getRolePermissionsByRoleId(roleId));
    }

    /**
     * 分配角色权限
     */
    @Log(value = "分配角色权限" , exception = "分配角色权限请求异常")
    @PostMapping(value = "/setRolePerms")
    @Operation(summary = "分配角色权限权限", description = "根据角色ID分配权限")
    public ApiResult<?> setRolePermissions(@Validated @RequestBody SysRolePermissionDTO rolePermissionDTO) {
        return ApiResult.condition(sysRolePermissionService.setRolePermissionsByRoleId(rolePermissionDTO));
    }

    // /**
    //  * 查询角色权限关联列表
    //  */
    // @Log(value = "角色权限关联", exception = "查询角色权限关联列表请求异常")
    // @GetMapping("/list")
    // @Operation(summary = "查询角色权限关联列表")
    // @Parameters({
    //         @Parameter(name = "pageNum", required = true, value = "当前页", in = ParameterIn.QUERY),
    //         @Parameter(name = "pageSize", required = true, value = "每页显示数据", in = ParameterIn.QUERY),
    //         @Parameter(name = "keyword", required = true, value = "模糊查询关键词", in = ParameterIn.QUERY),
    //         @Parameter(name = "startDate", required = true, value = "创建开始日期", in = ParameterIn.QUERY),
    //         @Parameter(name = "endDate", required = true, value = "创建结束日期", in = ParameterIn.QUERY),
    // })
    // public ApiResult<?> list(Search search, SysRolePermission sysRolePermission) {
    //     PageUtils list = sysRolePermissionService.listPage(search, sysRolePermission);
    //     return ApiResult.data(list);
    // }

    // /**
    //  * 获取角色权限关联详情
    //  */
    // @Log(value = "角色权限关联", exception = "获取角色权限关联详情请求异常")
    // @GetMapping(value = "/info/{id}")
    // @Operation(summary = "获取角色权限关联详情", description = "根据id获取角色权限关联详情")
    // @Parameters({
    //         @Parameter(name = "id", required = true, value = "ID", in = ParameterIn.PATH),
    // })
    // public ApiResult<?> getInfo(@PathVariable("id") Long id) {
    //     return ApiResult.data(sysRolePermissionService.getById(id));
    // }
    //
    // /**
    //  * 新增角色权限关联
    //  */
    // @Log(value = "角色权限关联", exception = "新增角色权限关联请求异常")
    // @PostMapping
    // @Operation(summary = "新增角色权限关联")
    // public ApiResult<?> add(@RequestBody SysRolePermission sysRolePermission) {
    //     return ApiResult.condition(sysRolePermissionService.save(sysRolePermission));
    // }
    //
    // /**
    //  * 修改角色权限关联
    //  */
    // @Log(value = "角色权限关联", exception = "修改角色权限关联请求异常")
    // @PutMapping
    // @Operation(summary = "修改角色权限关联")
    // public ApiResult<?> edit(@RequestBody SysRolePermission sysRolePermission) {
    //     return ApiResult.condition(sysRolePermissionService.updateById(sysRolePermission));
    // }
    //
    // /**
    //  * 删除角色权限关联
    //  */
    // @Log(value = "角色权限关联", exception = "删除角色权限关联请求异常")
    // @DeleteMapping
    // @Operation(summary = "删除角色权限关联")
    // public ApiResult<?> remove(@RequestBody Long[] ids) {
    //     return ApiResult.condition(sysRolePermissionService.removeByIds(Arrays.asList(ids)));
    // }

}
