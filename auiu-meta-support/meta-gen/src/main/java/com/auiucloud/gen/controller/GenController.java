package com.auiucloud.gen.controller;

import com.auiucloud.core.common.api.ApiResult;
import com.auiucloud.core.common.controller.BaseController;
import com.auiucloud.core.common.utils.FileUtil;
import com.auiucloud.core.database.model.Search;
import com.auiucloud.core.database.utils.PageUtils;
import com.auiucloud.core.log.annotation.Log;
import com.auiucloud.gen.domain.GenTable;
import com.auiucloud.gen.domain.GenTableColumn;
import com.auiucloud.gen.dto.GenTableDTO;
import com.auiucloud.gen.service.IGenTableColumnService;
import com.auiucloud.gen.service.IGenTableService;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author dries
 * @createDate 2022-08-16 13-29
 */
@Slf4j
@Tag(name = "代码管理")
@RestController
@RequiredArgsConstructor
@RequestMapping("/tools/gen")
public class GenController extends BaseController {

    private final IGenTableService genTableService;
    private final IGenTableColumnService genTableColumnService;

    @Operation(summary = "代码生成列表")
    @Log(value = "代码管理", exception = "数据源库表信息请求异常")
    @Parameters({
            @Parameter(name = "keyword", value = "数据源名称", in = ParameterIn.PATH),
            @Parameter(name = "pageNum", value = "当前页码", in = ParameterIn.QUERY),
            @Parameter(name = "pageSize", value = "显示条数", in = ParameterIn.QUERY),
            @Parameter(name = "tableName", value = "表名称", in = ParameterIn.QUERY),
            @Parameter(name = "tableComment", value = "表描述", in = ParameterIn.QUERY),
    })
    @GetMapping("/list")
    public ApiResult<?> list(Search search, GenTable genTable) {
        return ApiResult.data(genTableService.listPage(search, genTable));
    }


    @Operation(summary = "数据库列表")
    @Log(value = "代码管理", exception = "数据源库表信息请求异常")
    @Parameters({
            @Parameter(name = "keyword", value = "数据源名称", in = ParameterIn.PATH, required = true),
            @Parameter(name = "pageNum", value = "当前页码", in = ParameterIn.QUERY),
            @Parameter(name = "pageSize", value = "显示条数", in = ParameterIn.QUERY),
            @Parameter(name = "tableName", value = "表名称", in = ParameterIn.QUERY),
            @Parameter(name = "tableComment", value = "表描述", in = ParameterIn.QUERY),
    })
    @GetMapping("/db/list")
    public ApiResult<?> dataListByDsName(Search search, GenTable genTable) {
        List<TableInfo> list = genTableService.selectDbTableListByDsName(search.getKeyword(), genTable);
        return ApiResult.data(new PageUtils(search, list));
    }

    /**
     * 代码生成详情
     */
    @Log(value = "代码管理", exception = "代码生成详情请求异常")
    @Parameters({
            @Parameter(name = "tableId", value = "数据库ID", in = ParameterIn.PATH, required = true),
    })
    @GetMapping("/info/{tableId}")
    public ApiResult<?> getInfo(@PathVariable Long tableId) {
        GenTableDTO table = genTableService.getGenTableDTOById(tableId);
        List<GenTable> tables = genTableService.list();
        List<GenTableColumn> list = genTableColumnService.selectTableColumnsByTableId(tableId);
        Map<String, Object> map = new HashMap<>();
        map.put("info", table);
        map.put("tableColumns", list);
        map.put("tables", tables);
        return ApiResult.data(map);
    }

    /**
     * 修改代码生成信息
     */
    @Log(value = "代码管理", exception = "修改代码生成信息请求异常")
    @PutMapping
    public ApiResult<?> edit(@Validated @RequestBody GenTableDTO genTableDTO) {
        return ApiResult.condition(genTableService.editGenTableById(genTableDTO));
    }

    @Operation(summary = "导入表结构（保存）")
    @Log(value = "代码管理", exception = "导入表结构（保存）请求异常")
    @Parameters({
            @Parameter(name = "dsName", value = "数据源名称", in = ParameterIn.PATH, required = true),
            @Parameter(name = "tables", value = "表名称", paramType = "body"),
    })
    @PostMapping("/importTable/{dsName}")
    public ApiResult<?> importTable(@PathVariable String dsName, @RequestBody String[] tables) {
        genTableService.removeByTableNames(tables);
        List<GenTable> tableList = genTableService.selectDbTableListByNames(dsName, tables);
        genTableService.importGenTable(dsName, tableList);
        return ApiResult.success();
    }

    @Operation(summary = "预览代码")
    @Log(value = "代码管理", exception = "预览代码请求异常")
    @Parameters({
            @Parameter(name = "tableId", value = "数据表ID", in = ParameterIn.PATH, required = true)
    })
    @GetMapping("/preview/{tableId}")
    public ApiResult<?> preview(@PathVariable Long tableId) {
        Map<String, Object> map = genTableService.previewCode(tableId);
        return ApiResult.data(map);
    }

    @Operation(summary = "代码生成（下载方式）")
    @Log(value = "代码管理", exception = "代码生成（下载方式）请求异常")
    @Parameters({
            @Parameter(name = "tableId", value = "数据表ID", in = ParameterIn.PATH, required = true)
    })
    @GetMapping("/download/{tableId}")
    public void downloadGenCode(@PathVariable Long tableId,
                                HttpServletResponse response) throws IOException {
        byte[] bytes = genTableService.downloadGenCode(tableId);
        FileUtil.downloadFile(response, bytes, "code_" + System.currentTimeMillis() + ".zip");
    }

    @Operation(summary = "代码生成（自定义路径）")
    @Log(value = "代码管理", exception = "代码生成请求异常")
    @Parameters({
            @Parameter(name = "tableId", value = "数据表ID", in = ParameterIn.PATH, required = true)
    })
    @GetMapping("/{tableId}")
    public ApiResult<?> genCode(@PathVariable Long tableId) {
        genTableService.generatorCode(tableId);
        return ApiResult.success();
    }

    @Operation(summary = "代码批量生成")
    @Log(value = "代码管理", exception = "代码生成请求异常")
    @Parameters({
            @Parameter(name = "tableIds", value = "数据表ID", paramType = "body", required = true)
    })
    @PostMapping("/codeBatch")
    public void genCodeBatch(@RequestBody Long[] tableIds,
                             HttpServletResponse response) throws IOException {
        byte[] data = genTableService.downloadGenCode(tableIds);
        FileUtil.downloadFile(response, data, "code_" + System.currentTimeMillis() + ".zip");
    }

    /**
     * 删除代码生成
     */
    @Operation(summary = "删除代码生成")
    @Log(value = "代码管理", exception = "删除代码生成请求异常")
    @Parameters({
            @Parameter(name = "ids", value = "数据表ID", paramType = "body"),
    })
    @DeleteMapping
    public ApiResult<?> remove(@RequestBody Long[] ids) {
        return ApiResult.condition(genTableService.removeByIds(Arrays.asList(ids)));
    }

}
