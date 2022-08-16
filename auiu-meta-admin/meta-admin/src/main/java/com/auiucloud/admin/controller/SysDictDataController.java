package com.auiucloud.admin.controller;

import com.auiucloud.admin.domain.SysDictData;
import com.auiucloud.admin.service.ISysDictDataService;
import com.auiucloud.core.common.api.ApiResult;
import com.auiucloud.core.common.controller.BaseController;
import com.auiucloud.core.database.model.Search;
import com.auiucloud.core.log.annotation.Log;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

/**
 * 字典管理项
 *
 * @author dries
 * @createDate 2022-07-13 21-42
 */
@Slf4j
@Api(tags = "字典数据")
@RestController
@RequiredArgsConstructor
@RequestMapping("/dict/data")
public class SysDictDataController extends BaseController {

    private final ISysDictDataService dictDataService;

    @ApiOperation("字典数据列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum", value = "当前页码", paramType = "query"),
            @ApiImplicitParam(name = "pageSize", value = "显示条数", paramType = "query"),
            @ApiImplicitParam(name = "keyword", value = "模糊查询关键词", paramType = "query"),
            @ApiImplicitParam(name = "status", value = "状态(false-禁用 true-启用)", paramType = "query"),
            @ApiImplicitParam(name = "type", value = "字典类型", paramType = "query", required = true)
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
        List<SysDictData> data = dictDataService.selectDictDataByType(dictType);
        return ApiResult.data(data);
    }

    /**
     * 新增字典类型
     */
    @Log("字典数据")
    @PostMapping
    public ApiResult<?> add(@Validated @RequestBody SysDictData dictData) {
        return ApiResult.condition(dictDataService.save(dictData));
    }

    /**
     * 修改保存字典类型
     */
    @Log("字典数据")
    @PutMapping
    public ApiResult<?> edit(@Validated @RequestBody SysDictData dictData) {
        return ApiResult.condition(dictDataService.updateById(dictData));
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
