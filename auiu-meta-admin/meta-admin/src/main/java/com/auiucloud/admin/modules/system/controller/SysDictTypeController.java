package com.auiucloud.admin.modules.system.controller;

import com.auiucloud.admin.modules.system.domain.SysDictType;
import com.auiucloud.admin.modules.system.service.ISysDictTypeService;
import com.auiucloud.core.common.api.ApiResult;
import com.auiucloud.core.web.controller.BaseController;
import com.auiucloud.core.common.enums.QueryModeEnum;
import com.auiucloud.core.database.model.Search;
import com.auiucloud.core.database.utils.PageUtils;
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

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 字典管理
 *
 * @author dries
 * @createDate 2022-07-03 15-07
 */
@Slf4j
@Tag(name = "字典管理")
@RestController
@RequiredArgsConstructor
@RequestMapping("/dict/type")
public class SysDictTypeController extends BaseController {

    private final ISysDictTypeService dictTypeService;

    @Operation(summary = "字典列表")
    @Parameters({
            @Parameter(name = "queryMode", description = "查询模式", in = ParameterIn.QUERY),
            @Parameter(name = "pageNum", description = "当前页码", in = ParameterIn.QUERY),
            @Parameter(name = "pageSize", description = "显示条数", in = ParameterIn.QUERY),
            @Parameter(name = "dictName", description = "字典名称", in = ParameterIn.QUERY),
            @Parameter(name = "status", description = "状态(0-禁用 1-启用)", in = ParameterIn.QUERY),
            @Parameter(name = "dictType", description = "字典类型", in = ParameterIn.QUERY)
    })
    @GetMapping("/list")
    public ApiResult<?> list(Search search, SysDictType dictType) {
        QueryModeEnum mode = QueryModeEnum.getQueryModeByCode(search.getQueryMode());
        switch (mode) {
            case LIST:
                return ApiResult.data(dictTypeService.selectDictTypeList(dictType));
            case PAGE:
            default:
                PageUtils list = dictTypeService.listPage(search, dictType);
                return ApiResult.data(list);
        }

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
    @Log(value = "字典类型", exception = "获取系统部门详情请求异常")
    @Operation(summary = "新增字典类型")
    @PostMapping
    public ApiResult<?> add(@Validated @RequestBody SysDictType dict) {
        if (dictTypeService.checkDictTypeUnique(dict)) {
            return ApiResult.fail("新增字典'" + dict.getDictName() + "'失败，字典类型已存在");
        }
        if (dictTypeService.checkDictNameUnique(dict)) {
            return ApiResult.fail("新增字典'" + dict.getDictName() + "'失败，字典名称已存在");
        }
        return ApiResult.condition(dictTypeService.save(dict));
    }

    /**
     * 修改字典类型
     */
    @Log("字典类型")
    @Operation(summary = "修改字典类型")
    @PutMapping
    public ApiResult<?> edit(@Validated @RequestBody SysDictType dict) {
        if (dictTypeService.checkDictTypeUnique(dict)) {
            return ApiResult.fail("修改字典'" + dict.getDictName() + "'失败，字典类型已存在");
        }
        if (dictTypeService.checkDictNameUnique(dict)) {
            return ApiResult.fail("修改字典'" + dict.getDictName() + "'失败，字典名称已存在");
        }
        return ApiResult.condition(dictTypeService.updateById(dict));
    }

    /**
     * 删除字典类型
     */
    @Log("字典类型")
    @Operation(summary = "删除字典类型")
    @DeleteMapping("/{dictIds}")
    public ApiResult<?> remove(@PathVariable Long[] dictIds) {
        return ApiResult.success(dictTypeService.removeDictTypeByIds(dictIds));
    }

    /**
     * 获取字典选择框列表
     */
    @Operation(summary = "获取字典选择框列表")
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
