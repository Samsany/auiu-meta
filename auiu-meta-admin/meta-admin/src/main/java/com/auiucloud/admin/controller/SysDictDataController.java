package com.auiucloud.admin.controller;

import com.auiucloud.admin.domain.SysDictData;
import com.auiucloud.admin.service.ISysDictDataService;
import com.auiucloud.admin.service.ISysDictTypeService;
import com.auiucloud.admin.vo.SysDictVO;
import com.auiucloud.core.common.api.ApiResult;
import com.auiucloud.core.common.controller.BaseController;
import com.auiucloud.core.database.model.Search;
import com.auiucloud.core.log.annotation.Log;
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
 * 字典管理项
 *
 * @author dries
 * @createDate 2022-07-13 21-42
 */
@Slf4j
@Tag(name = "字典数据")
@RestController
@RequiredArgsConstructor
@RequestMapping("/dict/data")
public class SysDictDataController extends BaseController {

    private final ISysDictTypeService dictTypeService;
    private final ISysDictDataService dictDataService;

    @Operation(summary ="字典数据列表")
    @Parameters({
            @Parameter(name = "pageNum", description = "当前页码", in = ParameterIn.QUERY),
            @Parameter(name = "pageSize", description = "显示条数", in = ParameterIn.QUERY),
            @Parameter(name = "keyword", description = "模糊查询关键词", in = ParameterIn.QUERY),
            @Parameter(name = "status", description = "状态(false-禁用 true-启用)", in = ParameterIn.QUERY),
            @Parameter(name = "type", description = "字典类型", in = ParameterIn.QUERY, required = true)
    })
    @GetMapping("/list")
    public ApiResult<?> list(Search search, SysDictData dictData) {
        return ApiResult.data(dictDataService.listPage(search, dictData));
    }

    /**
     * 查询字典数据详细
     */
    @GetMapping("/{dictId}")
    public ApiResult<?> getInfo(@PathVariable Long dictId) {
        return ApiResult.data(dictDataService.getById(dictId));
    }

    /**
     * 根据字典类型查询字典数据信息
     */
    @GetMapping(value = "/type/{dictType}")
    public ApiResult<?> dictType(@PathVariable String dictType) {
        SysDictVO data = dictTypeService.selectDictInfoByType(dictType);
        return ApiResult.data(data);
    }

    /**
     * 新增字典类型
     */
    @Log("字典数据")
    @PostMapping
    public ApiResult<?> add(@Validated @RequestBody SysDictData dictData) {
        return ApiResult.condition(dictDataService.addSysDictData(dictData));
    }

    /**
     * 修改保存字典类型
     */
    @Log("字典数据")
    @PutMapping
    public ApiResult<?> edit(@Validated @RequestBody SysDictData dictData) {
        return ApiResult.condition(dictDataService.editSysDictData(dictData));
    }

    /**
     * 删除字典类型
     */
    @Log("字典类型")
    @DeleteMapping("/{dictCodes}")
    public ApiResult<?> remove(@PathVariable Long[] dictCodes) {
        return ApiResult.condition(dictDataService.removeBatchByIds(Arrays.asList(dictCodes)));
    }

}
