package com.auiucloud.gen.controller;

import com.auiucloud.core.common.api.ApiResult;
import com.auiucloud.core.common.controller.BaseController;
import com.auiucloud.core.database.model.Search;
import com.auiucloud.core.database.utils.PageUtils;
import com.auiucloud.core.log.annotation.Log;
import com.auiucloud.gen.domain.GenTable;
import com.auiucloud.gen.service.IGenTableColumnService;
import com.auiucloud.gen.service.IGenTableService;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;
import java.util.Map;

/**
 * @author dries
 * @createDate 2022-08-16 13-29
 */
@Slf4j
@Api(tags = "代码管理")
@RestController
@RequiredArgsConstructor
@RequestMapping("/tools/gen")
public class GenController extends BaseController {

    private final IGenTableService genTableService;
    private final IGenTableColumnService genTableColumnService;

    @ApiOperation("代码生成列表")
    @Log(value = "代码管理", exception = "数据源库表信息请求异常")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "keyword", value = "数据源名称", paramType = "path"),
            @ApiImplicitParam(name = "pageNum", value = "当前页码", paramType = "query"),
            @ApiImplicitParam(name = "pageSize", value = "显示条数", paramType = "query"),
            @ApiImplicitParam(name = "tableName", value = "表名称", paramType = "query"),
            @ApiImplicitParam(name = "tableComment", value = "表描述", paramType = "query"),
    })
    @GetMapping("/list")
    public ApiResult<?> list(Search search, @ApiIgnore GenTable genTable) {
        return ApiResult.data(genTableService.listPage(search, genTable));
    }


    @ApiOperation("数据库列表")
    @Log(value = "代码管理", exception = "数据源库表信息请求异常")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "keyword", value = "数据源名称", paramType = "path", required = true),
            @ApiImplicitParam(name = "pageNum", value = "当前页码", paramType = "query"),
            @ApiImplicitParam(name = "pageSize", value = "显示条数", paramType = "query"),
            @ApiImplicitParam(name = "tableName", value = "表名称", paramType = "query"),
            @ApiImplicitParam(name = "tableComment", value = "表描述", paramType = "query"),
    })
    @GetMapping("/db/list")
    public ApiResult<?> dataListByDsName(Search search, @ApiIgnore GenTable genTable) {
        List<TableInfo> list = genTableService.selectDbTableListByDsName(search.getKeyword(), genTable);
        return ApiResult.data(new PageUtils(search, list));
    }

    @ApiOperation("导入表结构（保存）")
    @Log(value = "代码管理", exception = "导入表结构（保存）请求异常")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "dsName", value = "数据源名称", paramType = "path", required = true),
            @ApiImplicitParam(name = "tables", value = "表名称", paramType = "body"),
    })
    @PostMapping("/importTable/{dsName}")
    public ApiResult<?> importTable(@PathVariable String dsName, @RequestBody String[] tables) {
        genTableService.removeByTableNames(tables);
        List<GenTable> tableList = genTableService.selectDbTableListByNames(dsName, tables);
        genTableService.importGenTable(dsName, tableList);
        return ApiResult.success();
    }

    @ApiOperation("预览代码")
    @Log(value = "代码管理", exception = "预览代码请求异常")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "tableId", value = "数据表ID", paramType = "path", required = true)
    })
    @GetMapping("/preview/{tableId}")
    public ApiResult<?> preview(@PathVariable String tableId) {
        Map<String, Object> map = genTableService.previewCode(tableId);
        return ApiResult.data(map);
    }

}
