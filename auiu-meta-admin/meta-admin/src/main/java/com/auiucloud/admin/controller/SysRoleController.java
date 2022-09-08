package com.auiucloud.admin.controller;

import com.auiucloud.admin.domain.SysRole;
import com.auiucloud.admin.service.ISysRoleService;
import com.auiucloud.core.common.api.ApiResult;
import com.auiucloud.core.common.controller.BaseController;
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
 * 系统角色控制器
 *
 * @author Dries
 * @date 2022-09-05 18:30:14
 */
@Api(value = "系统角色", tags = "系统角色")
@RestController
@RequiredArgsConstructor
@RequestMapping("/role")
public class SysRoleController extends BaseController {

    private final ISysRoleService sysRoleService;

    /**
     * 查询系统角色列表
     */
    @Log(value = "系统角色", exception = "查询系统角色列表请求异常")
    @GetMapping("/list")
    @ApiOperation(value = "查询系统角色列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum", required = true, value = "当前页", paramType = "form"),
            @ApiImplicitParam(name = "pageSize", required = true, value = "每页显示数据", paramType = "form"),
            @ApiImplicitParam(name = "keyword", required = true, value = "模糊查询关键词", paramType = "form"),
            @ApiImplicitParam(name = "startDate", required = true, value = "创建开始日期", paramType = "form"),
            @ApiImplicitParam(name = "endDate", required = true, value = "创建结束日期", paramType = "form"),
    })
    public ApiResult<?> list(Search search, @ApiIgnore SysRole sysRole) {
        PageUtils list = sysRoleService.listPage(search, sysRole);
        return ApiResult.data(list);
    }

    /**
     * 获取系统角色详情
     */
    @Log(value = "系统角色", exception = "获取系统角色详情请求异常")
    @GetMapping(value = "/info/{id}")
    @ApiOperation(value = "获取系统角色详情", notes = "根据id获取系统角色详情")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", required = true, value = "ID", paramType = "path"),
    })
    public ApiResult<?> getInfo(@PathVariable("id") Long id) {
        return ApiResult.data(sysRoleService.getById(id));
    }

    /**
     * 新增系统角色
     */
    @Log(value = "系统角色", exception = "新增系统角色请求异常")
    @PostMapping
    @ApiOperation(value = "新增系统角色")
    public ApiResult<?> add(@RequestBody SysRole sysRole) {
        return ApiResult.condition(sysRoleService.save(sysRole));
    }

    /**
     * 修改系统角色
     */
    @Log(value = "系统角色", exception = "修改系统角色请求异常")
    @PutMapping
    @ApiOperation(value = "修改系统角色")
    public ApiResult<?> edit(@RequestBody SysRole sysRole) {
        return ApiResult.condition(sysRoleService.updateById(sysRole));
    }

    /**
     * 删除系统角色
     */
    @Log(value = "系统角色", exception = "删除系统角色请求异常")
    @DeleteMapping
    @ApiOperation(value = "删除系统角色")
    public ApiResult<?> remove(@RequestBody Long[] ids) {
        return ApiResult.condition(sysRoleService.removeByIds(Arrays.asList(ids)));
    }

    /**
     * 导出系统角色
     */
    @Log(value = "系统角色", exception = "导出系统角色请求异常")
    @GetMapping("/export")
    @ApiOperation(value = "导出系统角色")
    public void export(Search search, @ApiIgnore SysRole sysRole, HttpServletResponse response) {
        List<SysRole> list = sysRoleService.selectSysRoleList(search, sysRole);
        String fileName = "系统角色" + System.currentTimeMillis();
        ExcelUtil.exportExcel(list, "sheet1", SysRole. class, fileName, response);
    }
}
