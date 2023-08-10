package com.auiucloud.component.sd.controller;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.auiucloud.component.sd.domain.SdFusionModelCategory;
import com.auiucloud.component.sd.domain.SdFusionModelCategory;
import com.auiucloud.component.sd.service.ISdFusionModelCategoryService;
import com.auiucloud.component.sd.service.ISdFusionModelCategoryService;
import com.auiucloud.core.common.api.ApiResult;
import com.auiucloud.core.common.enums.QueryModeEnum;
import com.auiucloud.core.common.model.dto.UpdateStatusDTO;
import com.auiucloud.core.database.model.Search;
import com.auiucloud.core.log.annotation.Log;
import com.auiucloud.core.web.controller.BaseController;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @author dries
 **/
@Tag(name = "融合模型管理")
@RestController
@AllArgsConstructor
@RequestMapping("/lora-cate")
public class SdFusionModelCateController extends BaseController {

    private final ISdFusionModelCategoryService sdFusionModelCategoryService;

    /**
     * 查询融合模型分类列表
     */
    @GetMapping("/list")
    @Operation(summary = "查询融合模型分类列表")
    @Parameters({
            @Parameter(name = "queryMode", description = "查询模式", in = ParameterIn.QUERY),
            @Parameter(name = "pageNum", description = "当前页", in = ParameterIn.QUERY),
            @Parameter(name = "pageSize", description = "每页显示数据", in = ParameterIn.QUERY),
            @Parameter(name = "keyword", description = "关键字", in = ParameterIn.QUERY),
    })
    public ApiResult<?> list(@Parameter(hidden = true) Search search, @Parameter(hidden = true) SdFusionModelCategory category) {
        QueryModeEnum mode = QueryModeEnum.getQueryModeByCode(search.getQueryMode());
        return switch (mode) {
            case LIST ->
                    ApiResult.data(sdFusionModelCategoryService.list(Wrappers.<SdFusionModelCategory>lambdaQuery()
                            .like(StrUtil.isNotBlank(search.getKeyword()), SdFusionModelCategory::getName, search.getKeyword())
                            .eq(ObjectUtil.isNotNull(search.getStatus()), SdFusionModelCategory::getStatus, search.getStatus())
                            .orderByDesc(SdFusionModelCategory::getSort)
                            .orderByDesc(SdFusionModelCategory::getCreateTime)
                            .orderByDesc(SdFusionModelCategory::getId)
                    ));
            default -> ApiResult.data(sdFusionModelCategoryService.listPage(search, category));
        };
    }

    /**
     * 获取融合模型分类详情
     */
    @GetMapping
    @Operation(summary = "获取融合模型分类详情", description = "根据id获取融合模型分类详情")
    @Parameters({
            @Parameter(name = "id", required = true, description = "ID", in = ParameterIn.QUERY),
    })
    public ApiResult<?> getInfo(@RequestParam Long id) {
        return ApiResult.data(sdFusionModelCategoryService.getById(id));
    }

    /**
     * 新增融合模型分类
     */
    @Log(value = "融合模型分类", exception = "新增融合模型分类请求异常")
    @PostMapping
    @Operation(summary = "新增融合模型分类")
    public ApiResult<?> add(@RequestBody SdFusionModelCategory category) {
        if (sdFusionModelCategoryService.checkFusionModelCategoryNameExist(category)) {
            return ApiResult.fail("新增'" + category.getName() + "'失败，名称已存在");
        }
        return ApiResult.condition(sdFusionModelCategoryService.save(category));
    }

    /**
     * 修改融合模型分类
     */
    @Log(value = "融合模型分类", exception = "修改融合模型分类请求异常")
    @PutMapping
    @Operation(summary = "修改融合模型分类")
    public ApiResult<?> edit(@RequestBody SdFusionModelCategory category) {
        if (sdFusionModelCategoryService.checkFusionModelCategoryNameExist(category)) {
            return ApiResult.fail("修改'" + category.getName() + "'失败，名称已存在");
        }
        return ApiResult.condition(sdFusionModelCategoryService.updateById(category));
    }

    /**
     * 设置融合模型分类状态
     */
    @Log(value = "融合模型分类", exception = "修改融合模型分类请求异常")
    @PutMapping("/set-status")
    @Operation(summary = "修改融合模型分类状态")
    public ApiResult<?> setFusionModelCategoryStatus(@Validated @RequestBody UpdateStatusDTO updateStatusDTO) {
        return ApiResult.condition(sdFusionModelCategoryService.setFusionModelCategoryStatus(updateStatusDTO));
    }

    /**
     * 删除融合模型分类
     */
    @Log(value = "融合模型分类", exception = "删除融合模型分类请求异常")
    @DeleteMapping
    @Parameter(name = "id", description = "融合模型分类ID", in = ParameterIn.QUERY)
    @Operation(summary = "删除融合模型分类")
    public ApiResult<?> remove(@RequestParam Long id) {
        return ApiResult.condition(sdFusionModelCategoryService.removeById(id));
    }
}
