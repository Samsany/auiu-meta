package com.auiucloud.admin.controller;

import com.auiucloud.admin.domain.SysLog;
import com.auiucloud.admin.service.ISysLogService;
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
 * 系统日志控制器
 *
 * @author Dries
 * @date 2022-09-09 23:22:02
 */
@Api(value = "系统日志", tags = "系统日志")
@RestController
@RequiredArgsConstructor
@RequestMapping("/log")
public class SysLogController extends BaseController {

    private final ISysLogService sysLogService;

    /**
     * 查询系统日志列表
     */
    @Log(value = "系统日志", exception = "查询系统日志列表请求异常")
    @GetMapping("/list")
    @ApiOperation(value = "查询系统日志列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum", required = true, value = "当前页", paramType = "query"),
            @ApiImplicitParam(name = "pageSize", required = true, value = "每页显示数据", paramType = "query"),
            @ApiImplicitParam(name = "keyword", required = true, value = "模糊查询关键词", paramType = "query"),
            @ApiImplicitParam(name = "startDate", required = true, value = "创建开始日期", paramType = "query"),
            @ApiImplicitParam(name = "endDate", required = true, value = "创建结束日期", paramType = "query"),
    })
    public ApiResult<?> list(Search search, @ApiIgnore SysLog sysLog) {
        PageUtils list = sysLogService.listPage(search, sysLog);
        return ApiResult.data(list);
    }

    /**
     * 获取系统日志详情
     */
    @Log(value = "系统日志", exception = "获取系统日志详情请求异常")
    @GetMapping(value = "/info/{id}")
    @ApiOperation(value = "获取系统日志详情", notes = "根据id获取系统日志详情")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", required = true, value = "ID", paramType = "path"),
    })
    public ApiResult<?> getInfo(@PathVariable("id") Long id) {
        return ApiResult.data(sysLogService.getById(id));
    }

    /**
     * 新增系统日志
     */
    @Log(value = "系统日志", exception = "新增系统日志请求异常")
    @PostMapping
    @ApiOperation(value = "新增系统日志")
    public ApiResult<?> add(@RequestBody SysLog sysLog) {
        return ApiResult.condition(sysLogService.save(sysLog));
    }

    /**
     * 修改系统日志
     */
    @Log(value = "系统日志", exception = "修改系统日志请求异常")
    @PutMapping
    @ApiOperation(value = "修改系统日志")
    public ApiResult<?> edit(@RequestBody SysLog sysLog) {
        return ApiResult.condition(sysLogService.updateById(sysLog));
    }

    /**
     * 删除系统日志
     */
    @Log(value = "系统日志", exception = "删除系统日志请求异常")
    @DeleteMapping
    @ApiOperation(value = "删除系统日志")
    public ApiResult<?> remove(@RequestBody Long[] ids) {
        return ApiResult.condition(sysLogService.removeByIds(Arrays.asList(ids)));
    }

    /**
     * 导出系统日志
     */
    @Log(value = "系统日志", exception = "导出系统日志请求异常")
    @GetMapping("/export")
    @ApiOperation(value = "导出系统日志")
    public void export(Search search, @ApiIgnore SysLog sysLog, HttpServletResponse response) {
        List<SysLog> list = sysLogService.selectSysLogList(search, sysLog);
        String fileName = "系统日志" + System.currentTimeMillis();
        ExcelUtil.exportExcel(list, "sheet1", SysLog. class, fileName, response);
    }

}
