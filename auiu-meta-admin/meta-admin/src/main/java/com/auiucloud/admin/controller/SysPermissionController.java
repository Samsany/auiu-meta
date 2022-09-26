package com.auiucloud.admin.controller;

import com.auiucloud.admin.domain.SysPermission;
import com.auiucloud.admin.service.ISysPermissionService;
import com.auiucloud.core.common.api.ApiResult;
import com.auiucloud.core.common.utils.poi.ExcelUtil;
import com.auiucloud.core.database.model.Search;
import com.auiucloud.core.database.utils.PageUtils;
import com.auiucloud.core.log.annotation.Log;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;

/**
 * @author dries
 * @createDate 2022-09-08 12-02
 */
@Api(value = "系统权限", tags = "系统权限")
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
    @ApiOperation(value = "查询系统权限列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum", required = true, value = "当前页", paramType = "form"),
            @ApiImplicitParam(name = "pageSize", required = true, value = "每页显示数据", paramType = "form"),
            @ApiImplicitParam(name = "keyword", required = true, value = "模糊查询关键词", paramType = "form"),
            @ApiImplicitParam(name = "startDate", required = true, value = "创建开始日期", paramType = "form"),
            @ApiImplicitParam(name = "endDate", required = true, value = "创建结束日期", paramType = "form"),
    })
    public ApiResult<?> list(Search search, @ApiIgnore SysPermission sysPermission) {
        PageUtils list = sysPermissionService.listPage(search, sysPermission);
        return ApiResult.data(list);
    }

    /**
     * 根据菜单ID获取系统权限列表
     */
    @Log(value = "系统权限", exception = "根据菜单ID获取系统权限请求异常")
    @GetMapping(value = "/{menuId}/list")
    @ApiOperation(value = "获取系统权限列表", notes = "根据菜单ID获取系统权限列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "menuId", required = true, value = "menuId", paramType = "path"),
    })
    public ApiResult<?> getPermissionListByMenuId(@PathVariable("menuId") Long menuId) {
        return ApiResult.data(sysPermissionService.getPermissionListByMenuId(menuId));
    }

    /**
     * 获取系统权限详情
     */
    @Log(value = "系统权限", exception = "获取系统权限详情请求异常")
    @GetMapping(value = "/info/{id}")
    @ApiOperation(value = "获取系统权限详情", notes = "根据id获取系统权限详情")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", required = true, value = "ID", paramType = "path"),
    })
    public ApiResult<?> getInfo(@PathVariable("id") Long id) {
        return ApiResult.data(sysPermissionService.getById(id));
    }

    /**
     * 新增系统权限
     */
    @Log(value = "系统权限", exception = "新增系统权限请求异常")
    @PostMapping
    @ApiOperation(value = "新增系统权限")
    public ApiResult<?> add(@RequestBody SysPermission sysPermission) {
        return ApiResult.condition(sysPermissionService.save(sysPermission));
    }

    /**
     * 修改系统权限
     */
    @Log(value = "系统权限", exception = "修改系统权限请求异常")
    @PutMapping
    @ApiOperation(value = "修改系统权限")
    public ApiResult<?> edit(@RequestBody SysPermission sysPermission) {
        return ApiResult.condition(sysPermissionService.updateById(sysPermission));
    }

    /**
     * 删除系统权限
     */
    @Log(value = "系统权限", exception = "删除系统权限请求异常")
    @DeleteMapping
    @ApiOperation(value = "删除系统权限")
    public ApiResult<?> remove(@RequestBody Long[] ids) {
        return ApiResult.condition(sysPermissionService.removeByIds(Arrays.asList(ids)));
    }

    /**
     * 导出系统权限
     */
    @Log(value = "系统权限", exception = "导出系统权限请求异常")
    @GetMapping("/export")
    @ApiOperation(value = "导出系统权限")
    public void export(Search search, @ApiIgnore SysPermission sysPermission, HttpServletResponse response) {
        List<SysPermission> list = sysPermissionService.selectSysPermissionList(search, sysPermission);
        String fileName = "系统权限" + System.currentTimeMillis();
        ExcelUtil.exportExcel(list, "sheet1", SysPermission. class,fileName, response);
    }

}
