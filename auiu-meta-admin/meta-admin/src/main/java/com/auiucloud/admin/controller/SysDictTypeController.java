package com.auiucloud.admin.controller;

import com.auiucloud.admin.domain.SysDictType;
import com.auiucloud.admin.service.ISysDictTypeService;
import com.auiucloud.core.common.api.ApiResult;
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
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 字典管理
 *
 * @author dries
 * @createDate 2022-07-03 15-07
 */
@Slf4j
@Api(tags = "字典管理")
@RestController
@RequiredArgsConstructor
@RequestMapping("/dict/type")
public class SysDictTypeController {

    private final ISysDictTypeService dictTypeService;

    @ApiOperation("字典列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum", value = "当前页码", paramType = "query"),
            @ApiImplicitParam(name = "pageSize", value = "显示条数", paramType = "query"),
            @ApiImplicitParam(name = "keyword", value = "模糊查询关键词", paramType = "query"),
            @ApiImplicitParam(name = "status", value = "状态(0-禁用 1-启用)", paramType = "query"),
            @ApiImplicitParam(name = "dictType", value = "字典类型", paramType = "query")
    })
    @GetMapping("/list")
    public ApiResult<?> list(Search search, @ApiIgnore SysDictType dictType) {
        return ApiResult.data(dictTypeService.listPage(search, dictType));
    }

    @Log("字典类型")
    @PostMapping("/export")
    public void export(HttpServletResponse response, SysDictType dictType) {
//        List<SysDictType> list = dictTypeService.list(new LambdaQueryWrapper<SysDictType>().setEntity(dictType));
//        ExcelUtil<SysDictType> util = new ExcelUtil<SysDictType>(SysDictType.class);
//        util.exportExcel(response, list, "字典类型");
    }

    /**
     * 查询字典类型详情
     */
    @GetMapping("/{dictId}")
    public ApiResult<?> getInfo(@PathVariable Long dictId) {
        return ApiResult.data(dictTypeService.getById(dictId));
    }

    /**
     * 新增字典类型
     */
    @Log("字典类型")
    @PostMapping
    public ApiResult<?> add(@Validated @RequestBody SysDictType dict) {
        if (dictTypeService.checkDictTypeUnique(dict)) {
            return ApiResult.fail("新增字典'" + dict.getDictName() + "'失败，字典类型已存在");
        }
        return ApiResult.condition(dictTypeService.save(dict));
    }

    /**
     * 修改字典类型
     */
    @Log("字典类型")
    @PutMapping
    public ApiResult<?> edit(@Validated @RequestBody SysDictType dict) {
        if (dictTypeService.checkDictNameUnique(dict)) {
            return ApiResult.fail("修改字典'" + dict.getDictName() + "'失败，字典类型已存在");
        }
        return ApiResult.condition(dictTypeService.updateById(dict));
    }

    /**
     * 删除字典类型
     */
    @Log("字典类型")
    @DeleteMapping("/{dictIds}")
    public ApiResult<?> remove(@PathVariable Long[] dictIds) {
        return ApiResult.success(dictTypeService.removeDictTypeByIds(dictIds));
    }

    /**
     * 获取字典选择框列表
     */
    @GetMapping("/option-select")
    public ApiResult<?> optionSelect() {
        List<SysDictType> dictTypes = dictTypeService.list();
        return ApiResult.data(dictTypes);
    }

//
//    /**
//     * 刷新字典缓存
//     */
//    @PreAuthorize("@ss.hasPermi('system:dict:remove')")
//    @Log(title = "字典类型", businessType = BusinessType.CLEAN)
//    @DeleteMapping("/refreshCache")
//    public AjaxResult refreshCache() {
//        dictTypeService.resetDictCache();
//        return AjaxResult.success();
//    }
//

//
//    @Log(title = "字典类型", businessType = BusinessType.IMPORT)
//    @PreAuthorize("@ss.hasPermi('system:dict:import')")
//    @PostMapping("/importData")
//    public AjaxResult importData(MultipartFile file, boolean updateSupport) throws Exception {
//        ExcelUtil<SysDictType> util = new ExcelUtil<>(SysDictType.class);
//        List<SysDictType> dictTypeList = util.importExcel(file.getInputStream());
//        String operName = getUsername();
//        String message = dictTypeService.importDictType(dictTypeList, updateSupport, operName);
//        return AjaxResult.success(message);
//    }
//
//    @PostMapping("/importTemplate")
//    public void importTemplate(HttpServletResponse response) {
//        ExcelUtil<SysDictType> util = new ExcelUtil<>(SysDictType.class);
//        util.importTemplateExcel(response, "字典数据");
//    }

}
