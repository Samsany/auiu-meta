package com.auiucloud.admin.controller;

import com.auiucloud.admin.domain.SysPermission;
import com.auiucloud.admin.service.ISysPermissionService;
import com.auiucloud.core.common.api.ApiResult;
import com.auiucloud.core.common.utils.poi.ExcelUtil;
import com.auiucloud.core.database.model.Search;
import com.auiucloud.core.database.utils.PageUtils;
import com.auiucloud.core.log.annotation.Log;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;

/**
 * @author dries
 * @createDate 2022-09-08 12-02
 */
@Tag(name = "系统权限")
@RestController
@RequiredArgsConstructor
@RequestMapping("/permission")
public class SysPermissionController {

    private final ISysPermissionService sysPermissionService;

    /**
     * 查询系统权限列表
     */
    @Log(value = "系统权限", exception = "查询系统权限列表请求异常")
    @GetMapping("/list")
    @Operation(summary = "查询系统权限列表")
    @Parameters({
            @Parameter(name = "pageNum", required = true, description = "当前页", in = ParameterIn.QUERY),
            @Parameter(name = "pageSize", required = true, description = "每页显示数据", in = ParameterIn.QUERY),
            @Parameter(name = "keyword", required = true, description = "模糊查询关键词", in = ParameterIn.QUERY),
            @Parameter(name = "startDate", required = true, description = "创建开始日期", in = ParameterIn.QUERY),
            @Parameter(name = "endDate", required = true, description = "创建结束日期", in = ParameterIn.QUERY),
    })
    public ApiResult<?> list(Search search, SysPermission sysPermission) {
        PageUtils list = sysPermissionService.listPage(search, sysPermission);
        return ApiResult.data(list);
    }

    /**
     * 根据菜单ID获取系统权限列表
     */
    @Log(value = "系统权限", exception = "根据菜单ID获取系统权限请求异常")
    @GetMapping(value = "/{menuId}/list")
    @Operation(summary = "获取系统权限列表", description = "根据菜单ID获取系统权限列表")
    @Parameters({
            @Parameter(name = "menuId", required = true, description = "menuId", in = ParameterIn.PATH),
    })
    public ApiResult<?> getPermissionListByMenuId(@PathVariable("menuId") Long menuId) {
        return ApiResult.data(sysPermissionService.getPermissionListByMenuId(menuId));
    }

    /**
     * 获取系统权限详情
     */
    @Log(value = "系统权限", exception = "获取系统权限详情请求异常")
    @GetMapping(value = "/info/{id}")
    @Operation(summary = "获取系统权限详情", description = "根据id获取系统权限详情")
    @Parameters({
            @Parameter(name = "id", required = true, description = "ID", in = ParameterIn.PATH),
    })
    public ApiResult<?> getInfo(@PathVariable("id") Long id) {
        return ApiResult.data(sysPermissionService.getById(id));
    }

    /**
     * 新增系统权限
     */
    @Log(value = "系统权限", exception = "新增系统权限请求异常")
    @PostMapping
    @Operation(summary = "新增系统权限")
    public ApiResult<?> add(@RequestBody SysPermission sysPermission) {
        return ApiResult.condition(sysPermissionService.save(sysPermission));
    }

    /**
     * 修改系统权限
     */
    @Log(value = "系统权限", exception = "修改系统权限请求异常")
    @PutMapping
    @Operation(summary = "修改系统权限")
    public ApiResult<?> edit(@RequestBody SysPermission sysPermission) {
        return ApiResult.condition(sysPermissionService.updateById(sysPermission));
    }

    /**
     * 删除系统权限
     */
    @Log(value = "系统权限", exception = "删除系统权限请求异常")
    @DeleteMapping
    @Operation(summary = "删除系统权限")
    public ApiResult<?> remove(@RequestBody Long[] ids) {
        return ApiResult.condition(sysPermissionService.removeByIds(Arrays.asList(ids)));
    }

    /**
     * 导出系统权限
     */
    @Log(value = "系统权限", exception = "导出系统权限请求异常")
    @GetMapping("/export")
    @Operation(summary = "导出系统权限")
    public void export(Search search, SysPermission sysPermission, HttpServletResponse response) {
        List<SysPermission> list = sysPermissionService.selectSysPermissionList(search, sysPermission);
        String fileName = "系统权限" + System.currentTimeMillis();
        ExcelUtil.exportExcel(list, "sheet1", SysPermission. class,fileName, response);
    }

}
