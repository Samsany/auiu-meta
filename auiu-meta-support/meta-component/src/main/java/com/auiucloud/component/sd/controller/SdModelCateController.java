package com.auiucloud.component.sd.controller;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.auiucloud.component.sd.domain.SdDrawCategory;
import com.auiucloud.component.sd.service.ISdDrawCategoryService;
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
@Tag(name = "Ai绘画模型管理")
@RestController
@AllArgsConstructor
@RequestMapping("/ai-draw-cate")
public class SdModelCateController extends BaseController {

    private final ISdDrawCategoryService sdDrawCategoryService;

    /**
     * 查询Ai绘画分类列表
     */
    @GetMapping("/list")
    @Operation(summary = "查询Ai绘画分类列表")
    @Parameters({
            @Parameter(name = "queryMode", description = "查询模式", in = ParameterIn.QUERY),
            @Parameter(name = "pageNum", description = "当前页", in = ParameterIn.QUERY),
            @Parameter(name = "pageSize", description = "每页显示数据", in = ParameterIn.QUERY),
            @Parameter(name = "keyword", description = "关键字", in = ParameterIn.QUERY),
    })
    public ApiResult<?> list(@Parameter(hidden = true) Search search, @Parameter(hidden = true) SdDrawCategory category) {
        QueryModeEnum mode = QueryModeEnum.getQueryModeByCode(search.getQueryMode());
        return switch (mode) {
            case LIST ->
                    ApiResult.data(sdDrawCategoryService.list(Wrappers.<SdDrawCategory>lambdaQuery()
                            .like(StrUtil.isNotBlank(search.getKeyword()), SdDrawCategory::getName, search.getKeyword())
                            .eq(ObjectUtil.isNotNull(search.getStatus()), SdDrawCategory::getStatus, search.getStatus())
                            .orderByDesc(SdDrawCategory::getSort)
                            .orderByDesc(SdDrawCategory::getCreateTime)
                            .orderByDesc(SdDrawCategory::getId)
                    ));
            default -> ApiResult.data(sdDrawCategoryService.listPage(search, category));
        };
    }

    /**
     * 获取Ai绘画分类详情
     */
    @GetMapping
    @Operation(summary = "获取Ai绘画分类详情", description = "根据id获取Ai绘画分类详情")
    @Parameters({
            @Parameter(name = "id", required = true, description = "ID", in = ParameterIn.QUERY),
    })
    public ApiResult<?> getInfo(@RequestParam Long id) {
        return ApiResult.data(sdDrawCategoryService.getById(id));
    }

    /**
     * 新增Ai绘画分类
     */
    @Log(value = "Ai绘画分类", exception = "新增Ai绘画分类请求异常")
    @PostMapping
    @Operation(summary = "新增Ai绘画分类")
    public ApiResult<?> add(@RequestBody SdDrawCategory category) {
        if (sdDrawCategoryService.checkSdModelCategoryNameExist(category)) {
            return ApiResult.fail("新增'" + category.getName() + "'失败，名称已存在");
        }
        return ApiResult.condition(sdDrawCategoryService.save(category));
    }

    /**
     * 修改Ai绘画分类
     */
    @Log(value = "Ai绘画分类", exception = "修改Ai绘画分类请求异常")
    @PutMapping
    @Operation(summary = "修改Ai绘画分类")
    public ApiResult<?> edit(@RequestBody SdDrawCategory category) {
        if (sdDrawCategoryService.checkSdModelCategoryNameExist(category)) {
            return ApiResult.fail("修改'" + category.getName() + "'失败，名称已存在");
        }
        return ApiResult.condition(sdDrawCategoryService.updateById(category));
    }

    /**
     * 设置Ai绘画分类状态
     */
    @Log(value = "Ai绘画分类", exception = "修改Ai绘画分类请求异常")
    @PutMapping("/set-status")
    @Operation(summary = "修改Ai绘画分类状态")
    public ApiResult<?> setSdModelCategoryStatus(@Validated @RequestBody UpdateStatusDTO updateStatusDTO) {
        return ApiResult.condition(sdDrawCategoryService.setSdModelCategoryStatus(updateStatusDTO));
    }

    /**
     * 删除Ai绘画分类
     */
    @Log(value = "Ai绘画分类", exception = "删除Ai绘画分类请求异常")
    @DeleteMapping
    @Parameter(name = "id", description = "Ai绘画分类ID", in = ParameterIn.QUERY)
    @Operation(summary = "删除Ai绘画分类")
    public ApiResult<?> remove(@RequestParam Long id) {
        return ApiResult.condition(sdDrawCategoryService.removeById(id));
    }
}
