package com.auiucloud.gen.controller;

import com.auiucloud.core.common.api.ApiResult;
import com.auiucloud.core.database.model.Search;
import com.auiucloud.core.log.annotation.Log;
import com.auiucloud.gen.domain.SysDataSource;
import com.auiucloud.gen.dto.DataSourceConnectDTO;
import com.auiucloud.gen.service.ISysDataSourceService;
import com.auiucloud.gen.vo.DataSourceVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;

/**
 * @author dries
 * @createDate 2022-08-16 16-11
 */
@Slf4j
@Tag(name = "数据源管理")
@RestController
@RequiredArgsConstructor
@RequestMapping("/tools/datasource")
public class SysDataSourceController {

    private final ISysDataSourceService dataSourceService;

    @Operation(summary ="数据源列表")
    @Log(value = "数据源管理", exception = "数据源列表请求异常")
    @Parameters({
            @Parameter(name = "pageNum", value = "当前页码", in = ParameterIn.QUERY),
            @Parameter(name = "pageSize", value = "显示条数", in = ParameterIn.QUERY),
            @Parameter(name = "keyword", value = "模糊查询关键词", in = ParameterIn.QUERY),
            @Parameter(name = "status", value = "状态(0-禁用 1-启用)", in = ParameterIn.QUERY),
    })
    @GetMapping("/list")
    public ApiResult<?> list(Search search, SysDataSource dataSource) {
        return ApiResult.data(dataSourceService.listPage(search, dataSource));
    }

    /**
     * 数据源连接测试
     */
    @Operation(summary ="数据源连接测试")
    @Log(value = "数据源管理", exception = "数据源连接测试请求异常")
    @PostMapping("/connect")
    public ApiResult<?> connectTest(@Validated @RequestBody DataSourceConnectDTO dataSource) {
        return ApiResult.data(dataSourceService.connectTest(dataSource));
    }

    /**
     * 查询数据源详情
     */
    @Operation(summary ="数据源详情")
    @Log(value = "数据源管理", exception = "数据源详情请求异常")
    @Parameter(name = "id", value = "数据源ID", in = ParameterIn.PATH)
    @GetMapping("/{id}")
    public ApiResult<?> getInfo(@PathVariable Long id) {
        SysDataSource sysDataSource = dataSourceService.getById(id);
        return ApiResult.data(DataSourceVO.convertDataSource.apply(sysDataSource));
    }

    /**
     * 新增数据源
     */
    @Operation(summary ="新增数据源")
    @Log(value = "数据源管理", exception = "新增数据源请求异常")
    @PostMapping
    public ApiResult<?> add(@Validated @RequestBody SysDataSource dataSource) {
        return ApiResult.condition(dataSourceService.addDataSource(dataSource));
    }

    /**
     * 修改数据源
     */
    @Operation(summary ="修改数据源")
    @Log(value = "数据源管理", exception = "修改数据源请求异常")
    @PutMapping
    public ApiResult<?> edit(@Validated @RequestBody SysDataSource dataSource) {
        return ApiResult.condition(dataSourceService.updateDataSourceById(dataSource));
    }

    /**
     * 删除数据源
     */
    @Operation(summary ="删除数据源")
    @Log(value = "数据源管理", exception = "删除数据源请求异常")
    @Parameters({
            @Parameter(name = "ids", value = "数据源编号", paramType = "body"),
    })
    @DeleteMapping
    public ApiResult<?> remove(@RequestBody Long[] ids) {
        return ApiResult.condition(dataSourceService.removeDataSourceByIds(Arrays.asList(ids)));
    }

}
