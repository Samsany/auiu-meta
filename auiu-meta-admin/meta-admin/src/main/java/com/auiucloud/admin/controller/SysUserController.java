package com.auiucloud.admin.controller;

import com.auiucloud.admin.domain.SysUser;
import com.auiucloud.admin.service.ISysUserService;
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
 * 系统用户控制器
 *
 * @author Dries
 * @date 2022-10-18 15:08:14
 */
@Api(value = "系统用户", tags = "系统用户")
@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class SysUserController extends BaseController {

    private final ISysUserService sysUserService;

    /**
     * 查询系统用户列表
     */
    @Log(value = "系统用户", exception = "查询系统用户列表请求异常")
    @GetMapping("/list")
    @ApiOperation(value = "查询系统用户列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum", value = "当前页", paramType = "query"),
            @ApiImplicitParam(name = "pageSize", value = "每页显示数据", paramType = "query"),
            @ApiImplicitParam(name = "keyword",  value = "模糊查询关键词", paramType = "query"),
            @ApiImplicitParam(name = "startDate", value = "开始日期", paramType = "query"),
            @ApiImplicitParam(name = "endDate", value = "结束日期", paramType = "query"),
    })
    public ApiResult<?> list(Search search, @ApiIgnore SysUser sysUser) {
        PageUtils list = sysUserService.listPage(search, sysUser);
        return ApiResult.data(list);
    }

    /**
     * 获取系统用户详情
     */
    @Log(value = "系统用户", exception = "获取系统用户详情请求异常")
    @GetMapping(value = "/info/{id}")
    @ApiOperation(value = "获取系统用户详情", notes = "根据id获取系统用户详情")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", required = true, value = "ID", paramType = "path"),
    })
    public ApiResult<?> getInfo(@PathVariable("id") Long id) {
        return ApiResult.data(sysUserService.getById(id));
    }

    /**
     * 新增系统用户
     */
    @Log(value = "系统用户", exception = "新增系统用户请求异常")
    @PostMapping
    @ApiOperation(value = "新增系统用户")
    public ApiResult<?> add(@RequestBody SysUser sysUser) {
        return ApiResult.condition(sysUserService.save(sysUser));
    }

    /**
     * 修改系统用户
     */
    @Log(value = "系统用户", exception = "修改系统用户请求异常")
    @PutMapping
    @ApiOperation(value = "修改系统用户")
    public ApiResult<?> edit(@RequestBody SysUser sysUser) {
        return ApiResult.condition(sysUserService.updateById(sysUser));
    }

    /**
     * 删除系统用户
     */
    @Log(value = "系统用户", exception = "删除系统用户请求异常")
    @DeleteMapping
    @ApiOperation(value = "删除系统用户")
    public ApiResult<?> remove(@RequestBody Long[] ids) {
        return ApiResult.condition(sysUserService.removeByIds(Arrays.asList(ids)));
    }

    /**
     * 导出系统用户
     */
    @Log(value = "系统用户", exception = "导出系统用户请求异常")
    @GetMapping("/export")
    @ApiOperation(value = "导出系统用户")
    public void export(Search search, @ApiIgnore SysUser SysUser, HttpServletResponse response) {
        List<SysUser> list = sysUserService.selectSysUserList(search, SysUser);
        String fileName = "系统用户" + System.currentTimeMillis();
        ExcelUtil.exportExcel(list, "sheet1", SysUser.class, fileName, response);
    }

}
