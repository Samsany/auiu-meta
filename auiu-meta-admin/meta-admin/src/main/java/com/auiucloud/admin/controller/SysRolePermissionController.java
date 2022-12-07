package com.auiucloud.admin.controller;

import com.auiucloud.admin.dto.SysRolePermissionDTO;
import com.auiucloud.admin.service.ISysRolePermissionService;
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
@Api(value = "角色权限关联", tags = "角色权限关联")
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
    @ApiOperation(value = "获取角色权限列表", notes = "根据角色ID查询分配的权限列表")
    public ApiResult<?> getRolePermissions(@PathVariable Long roleId) {
        return ApiResult.data(sysRolePermissionService.getRolePermissionsByRoleId(roleId));
    }

    /**
     * 分配角色权限
     */
    @Log(value = "分配角色权限" , exception = "分配角色权限请求异常")
    @PostMapping(value = "/setRolePerms")
    @ApiOperation(value = "分配角色权限权限", notes = "根据角色ID分配权限")
    public ApiResult<?> setRolePermissions(@Validated @RequestBody SysRolePermissionDTO rolePermissionDTO) {
        return ApiResult.condition(sysRolePermissionService.setRolePermissionsByRoleId(rolePermissionDTO));
    }

    // /**
    //  * 查询角色权限关联列表
    //  */
    // @Log(value = "角色权限关联", exception = "查询角色权限关联列表请求异常")
    // @GetMapping("/list")
    // @ApiOperation(value = "查询角色权限关联列表")
    // @ApiImplicitParams({
    //         @ApiImplicitParam(name = "pageNum", required = true, value = "当前页", paramType = "query"),
    //         @ApiImplicitParam(name = "pageSize", required = true, value = "每页显示数据", paramType = "query"),
    //         @ApiImplicitParam(name = "keyword", required = true, value = "模糊查询关键词", paramType = "query"),
    //         @ApiImplicitParam(name = "startDate", required = true, value = "创建开始日期", paramType = "query"),
    //         @ApiImplicitParam(name = "endDate", required = true, value = "创建结束日期", paramType = "query"),
    // })
    // public ApiResult<?> list(Search search, @ApiIgnore SysRolePermission sysRolePermission) {
    //     PageUtils list = sysRolePermissionService.listPage(search, sysRolePermission);
    //     return ApiResult.data(list);
    // }

    // /**
    //  * 获取角色权限关联详情
    //  */
    // @Log(value = "角色权限关联", exception = "获取角色权限关联详情请求异常")
    // @GetMapping(value = "/info/{id}")
    // @ApiOperation(value = "获取角色权限关联详情", notes = "根据id获取角色权限关联详情")
    // @ApiImplicitParams({
    //         @ApiImplicitParam(name = "id", required = true, value = "ID", paramType = "path"),
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
    // @ApiOperation(value = "新增角色权限关联")
    // public ApiResult<?> add(@RequestBody SysRolePermission sysRolePermission) {
    //     return ApiResult.condition(sysRolePermissionService.save(sysRolePermission));
    // }
    //
    // /**
    //  * 修改角色权限关联
    //  */
    // @Log(value = "角色权限关联", exception = "修改角色权限关联请求异常")
    // @PutMapping
    // @ApiOperation(value = "修改角色权限关联")
    // public ApiResult<?> edit(@RequestBody SysRolePermission sysRolePermission) {
    //     return ApiResult.condition(sysRolePermissionService.updateById(sysRolePermission));
    // }
    //
    // /**
    //  * 删除角色权限关联
    //  */
    // @Log(value = "角色权限关联", exception = "删除角色权限关联请求异常")
    // @DeleteMapping
    // @ApiOperation(value = "删除角色权限关联")
    // public ApiResult<?> remove(@RequestBody Long[] ids) {
    //     return ApiResult.condition(sysRolePermissionService.removeByIds(Arrays.asList(ids)));
    // }

}
