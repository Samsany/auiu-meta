package com.auiucloud.admin.controller;

import com.auiucloud.admin.domain.SysLog;
import com.auiucloud.admin.service.ISysLogService;
import com.auiucloud.core.common.api.ApiResult;
import com.auiucloud.core.common.controller.BaseController;
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
 * 系统日志控制器
 *
 * @author Dries
 * @date 2022-09-09 23:22:02
 */
@Tag(name = "系统日志")
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
    @Operation(summary = "查询系统日志列表")
    @Parameters({
            @Parameter(name = "pageNum", required = true, description = "当前页", in = ParameterIn.QUERY),
            @Parameter(name = "pageSize", required = true, description = "每页显示数据", in = ParameterIn.QUERY),
            @Parameter(name = "keyword", required = true, description = "模糊查询关键词", in = ParameterIn.QUERY),
            @Parameter(name = "startDate", required = true, description = "创建开始日期", in = ParameterIn.QUERY),
            @Parameter(name = "endDate", required = true, description = "创建结束日期", in = ParameterIn.QUERY),
    })
    public ApiResult<?> list(Search search, SysLog sysLog) {
        PageUtils list = sysLogService.listPage(search, sysLog);
        return ApiResult.data(list);
    }

    /**
     * 获取系统日志详情
     */
    @Log(value = "系统日志", exception = "获取系统日志详情请求异常")
    @GetMapping(value = "/info/{id}")
    @Operation(summary = "获取系统日志详情", description = "根据id获取系统日志详情")
    @Parameters({
            @Parameter(name = "id", required = true, description = "ID", in = ParameterIn.PATH),
    })
    public ApiResult<?> getInfo(@PathVariable("id") Long id) {
        return ApiResult.data(sysLogService.getById(id));
    }

    /**
     * 新增系统日志
     */
    @Log(value = "系统日志", exception = "新增系统日志请求异常")
    @PostMapping
    @Operation(summary = "新增系统日志")
    public ApiResult<?> add(@RequestBody SysLog sysLog) {
        return ApiResult.condition(sysLogService.save(sysLog));
    }

    /**
     * 修改系统日志
     */
    @Log(value = "系统日志", exception = "修改系统日志请求异常")
    @PutMapping
    @Operation(summary = "修改系统日志")
    public ApiResult<?> edit(@RequestBody SysLog sysLog) {
        return ApiResult.condition(sysLogService.updateById(sysLog));
    }

    /**
     * 删除系统日志
     */
    @Log(value = "系统日志", exception = "删除系统日志请求异常")
    @DeleteMapping
    @Operation(summary = "删除系统日志")
    public ApiResult<?> remove(@RequestBody Long[] ids) {
        return ApiResult.condition(sysLogService.removeByIds(Arrays.asList(ids)));
    }

    /**
     * 导出系统日志
     */
    @Log(value = "系统日志", exception = "导出系统日志请求异常")
    @GetMapping("/export")
    @Operation(summary = "导出系统日志")
    public void export(Search search, SysLog sysLog, HttpServletResponse response) {
        List<SysLog> list = sysLogService.selectSysLogList(search, sysLog);
        String fileName = "系统日志" + System.currentTimeMillis();
        ExcelUtil.exportExcel(list, "sheet1", SysLog. class, fileName, response);
    }

}
