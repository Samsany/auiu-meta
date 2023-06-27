package com.auiucloud.component.sd.controller;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.auiucloud.component.sd.domain.SdDrawStyleCategory;
import com.auiucloud.component.sd.service.ISdDrawStyleCategoryService;
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
@Tag(name = "绘画风格管理")
@RestController
@AllArgsConstructor
@RequestMapping("/draw-style-cate")
public class SdDrawStyleCateController extends BaseController {

    private final ISdDrawStyleCategoryService sdDrawStyleCategoryService;

    /**
     * 查询绘画风格分类列表
     */
    @Log(value = "绘画风格分类", exception = "查询绘画风格分类列表请求异常")
    @GetMapping("/list")
    @Operation(summary = "查询绘画风格分类列表")
    @Parameters({
            @Parameter(name = "queryMode", description = "查询模式", in = ParameterIn.QUERY),
            @Parameter(name = "pageNum", description = "当前页", in = ParameterIn.QUERY),
            @Parameter(name = "pageSize", description = "每页显示数据", in = ParameterIn.QUERY),
            @Parameter(name = "keyword", description = "关键字", in = ParameterIn.QUERY),
    })
    public ApiResult<?> list(@Parameter(hidden = true) Search search, @Parameter(hidden = true) SdDrawStyleCategory drawStyle) {
        QueryModeEnum mode = QueryModeEnum.getQueryModeByCode(search.getQueryMode());
        return switch (mode) {
            case LIST ->
                    ApiResult.data(sdDrawStyleCategoryService.list(Wrappers.<SdDrawStyleCategory>lambdaQuery()
                            .like(StrUtil.isNotBlank(search.getKeyword()), SdDrawStyleCategory::getName, search.getKeyword())
                            .eq(ObjectUtil.isNotNull(search.getStatus()), SdDrawStyleCategory::getStatus, search.getStatus())
                            .orderByDesc(SdDrawStyleCategory::getSort)
                            .orderByDesc(SdDrawStyleCategory::getCreateTime)
                            .orderByDesc(SdDrawStyleCategory::getId)
                    ));
            default -> ApiResult.data(sdDrawStyleCategoryService.listPage(search, drawStyle));
        };
    }

    /**
     * 获取绘画风格分类详情
     */
    @Log(value = "绘画风格分类", exception = "获取绘画风格分类详情请求异常")
    @GetMapping
    @Operation(summary = "获取绘画风格分类详情", description = "根据id获取绘画风格分类详情")
    @Parameters({
            @Parameter(name = "id", required = true, description = "ID", in = ParameterIn.QUERY),
    })
    public ApiResult<?> getInfo(@RequestParam Long id) {
        return ApiResult.data(sdDrawStyleCategoryService.getById(id));
    }

    /**
     * 新增绘画风格分类
     */
    @Log(value = "绘画风格分类", exception = "新增绘画风格分类请求异常")
    @PostMapping
    @Operation(summary = "新增绘画风格分类")
    public ApiResult<?> add(@RequestBody SdDrawStyleCategory drawStyle) {
        if (sdDrawStyleCategoryService.checkDrawStyleNameExist(drawStyle)) {
            return ApiResult.fail("新增'" + drawStyle.getName() + "'失败，名称已存在");
        }
        return ApiResult.condition(sdDrawStyleCategoryService.save(drawStyle));
    }

    /**
     * 修改绘画风格分类
     */
    @Log(value = "绘画风格分类", exception = "修改绘画风格分类请求异常")
    @PutMapping
    @Operation(summary = "修改绘画风格分类")
    public ApiResult<?> edit(@RequestBody SdDrawStyleCategory drawStyle) {
        if (sdDrawStyleCategoryService.checkDrawStyleNameExist(drawStyle)) {
            return ApiResult.fail("修改'" + drawStyle.getName() + "'失败，名称已存在");
        }
        return ApiResult.condition(sdDrawStyleCategoryService.updateById(drawStyle));
    }

    /**
     * 设置绘画风格分类状态
     */
    @Log(value = "绘画风格分类", exception = "修改绘画风格分类请求异常")
    @PutMapping("/set-status")
    @Operation(summary = "修改绘画风格分类状态")
    public ApiResult<?> setDrawStyleStatus(@Validated @RequestBody UpdateStatusDTO updateStatusDTO) {
        return ApiResult.condition(sdDrawStyleCategoryService.setDrawStyleStatus(updateStatusDTO));
    }

    /**
     * 删除绘画风格分类
     */
    @Log(value = "绘画风格分类", exception = "删除绘画风格分类请求异常")
    @DeleteMapping
    @Parameter(name = "id", description = "绘画风格分类ID", in = ParameterIn.QUERY)
    @Operation(summary = "删除绘画风格分类")
    public ApiResult<?> remove(@RequestParam Long id) {
        return ApiResult.condition(sdDrawStyleCategoryService.removeById(id));
    }
}
