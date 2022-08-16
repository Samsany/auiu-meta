package com.auiucloud.gen.controller;

import com.auiucloud.core.common.api.ApiResult;
import com.auiucloud.core.database.model.Search;
import com.auiucloud.core.log.annotation.Log;
import com.auiucloud.gen.domain.SysDataSource;
import com.auiucloud.gen.service.ISysDataSourceService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.Arrays;

/**
 * @author dries
 * @createDate 2022-08-16 16-11
 */
@Slf4j
@Api(tags = "数据源管理")
@RestController
@RequiredArgsConstructor
@RequestMapping("/tools/datasource")
public class SysDataSourceController {

    private final ISysDataSourceService dataSourceService;

    @ApiOperation("数据源列表")
    @Log(value = "数据源管理", exception = "数据源列表请求异常")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum", value = "当前页码", paramType = "query"),
            @ApiImplicitParam(name = "pageSize", value = "显示条数", paramType = "query"),
            @ApiImplicitParam(name = "keyword", value = "模糊查询关键词", paramType = "query"),
            @ApiImplicitParam(name = "status", value = "状态(0-禁用 1-启用)", paramType = "query"),
    })
    @GetMapping("/list")
    public ApiResult<?> list(Search search, @ApiIgnore SysDataSource dataSource) {
        return ApiResult.data(dataSourceService.listPage(search, dataSource));
    }

    /**
     * 查询数据源详情
     */
    @ApiOperation("数据源详情")
    @Log(value = "数据源管理", exception = "数据源详情请求异常")
    @ApiImplicitParam(name = "id", value = "数据源ID", paramType = "path")
    @GetMapping("/{id}")
    public ApiResult<?> getInfo(@PathVariable Long id) {
        return ApiResult.data(dataSourceService.getById(id));
    }

    /**
     * 新增数据源
     */
    @ApiOperation("新增数据源")
    @Log(value = "数据源管理", exception = "新增数据源请求异常")
    @PostMapping
    public ApiResult<?> add(@Validated @RequestBody SysDataSource dataSource) {
        if (dataSourceService.checkDataSourceNameUnique(dataSource)) {
            return ApiResult.fail("新增数据源'" + dataSource.getName() + "'失败，数据源已存在");
        }
        return ApiResult.condition(dataSourceService.save(dataSource));
    }

    /**
     * 修改数据源
     */
    @ApiOperation("修改数据源")
    @Log(value = "数据源管理", exception = "修改数据源请求异常")
    @PutMapping
    public ApiResult<?> edit(@Validated @RequestBody SysDataSource dataSource) {
        if (dataSourceService.checkDataSourceNameUnique(dataSource)) {
            return ApiResult.fail("修改数据源'" + dataSource.getName() + "'失败，数据源已存在");
        }
        return ApiResult.condition(dataSourceService.updateById(dataSource));
    }

    /**
     * 删除数据源
     */
    @ApiOperation("删除数据源")
    @Log(value = "数据源管理", exception = "删除数据源请求异常")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "ids", required = true, value = "多个用,号隔开", paramType = "query")
    })
    @DeleteMapping
    public ApiResult<?> remove(@RequestParam Long[] ids) {
        return ApiResult.condition(dataSourceService.removeByIds(Arrays.asList(ids)));
    }

}
