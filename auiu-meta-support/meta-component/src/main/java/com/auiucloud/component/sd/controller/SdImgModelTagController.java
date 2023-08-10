package com.auiucloud.component.sd.controller;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.auiucloud.component.sd.domain.SdDrawCategory;
import com.auiucloud.component.sd.domain.SdImageTag;
import com.auiucloud.component.sd.service.ISdImageTagService;
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
@Tag(name = "AI绘画推荐作品标签模型管理")
@RestController
@AllArgsConstructor
@RequestMapping("/ai-draw-img-tag")
public class SdImgModelTagController extends BaseController {

    private final ISdImageTagService sdImageTagService;

    /**
     * 查询AI绘画推荐作品标签分类列表
     */
    @GetMapping("/list")
    @Operation(summary = "查询AI绘画推荐作品标签分类列表")
    @Parameters({
            @Parameter(name = "queryMode", description = "查询模式", in = ParameterIn.QUERY),
            @Parameter(name = "pageNum", description = "当前页", in = ParameterIn.QUERY),
            @Parameter(name = "pageSize", description = "每页显示数据", in = ParameterIn.QUERY),
            @Parameter(name = "keyword", description = "关键字", in = ParameterIn.QUERY),
    })
    public ApiResult<?> list(@Parameter(hidden = true) Search search, @Parameter(hidden = true) SdImageTag tag) {
        QueryModeEnum mode = QueryModeEnum.getQueryModeByCode(search.getQueryMode());
        return switch (mode) {
            case LIST -> ApiResult.data(sdImageTagService.list(Wrappers.<SdImageTag>lambdaQuery()
                    .like(StrUtil.isNotBlank(search.getKeyword()), SdImageTag::getTitle, search.getKeyword())
                    .eq(ObjectUtil.isNotNull(search.getStatus()), SdImageTag::getStatus, search.getStatus())
                    .orderByDesc(SdImageTag::getSort)
                    .orderByDesc(SdImageTag::getId)
            ));
            default -> ApiResult.data(sdImageTagService.listPage(search, tag));
        };
    }

    /**
     * 获取AI绘画推荐作品标签分类详情
     */
    @GetMapping
    @Operation(summary = "获取AI绘画推荐作品标签分类详情", description = "根据id获取AI绘画推荐作品标签分类详情")
    @Parameters({
            @Parameter(name = "id", required = true, description = "ID", in = ParameterIn.QUERY),
    })
    public ApiResult<?> getInfo(@RequestParam Long id) {
        return ApiResult.data(sdImageTagService.getById(id));
    }

    /**
     * 新增AI绘画推荐作品标签分类
     */
    @Log(value = "AI绘画推荐作品标签分类", exception = "新增AI绘画推荐作品标签分类请求异常")
    @PostMapping
    @Operation(summary = "新增AI绘画推荐作品标签分类")
    public ApiResult<?> add(@RequestBody SdImageTag tag) {
        if (sdImageTagService.checkSdImageModelTagNameExist(tag)) {
            return ApiResult.fail("新增'" + tag.getTitle() + "'失败，名称已存在");
        }
        return ApiResult.condition(sdImageTagService.save(tag));
    }

    /**
     * 修改AI绘画推荐作品标签分类
     */
    @Log(value = "AI绘画推荐作品标签分类", exception = "修改AI绘画推荐作品标签分类请求异常")
    @PutMapping
    @Operation(summary = "修改AI绘画推荐作品标签分类")
    public ApiResult<?> edit(@RequestBody SdImageTag tag) {
        if (sdImageTagService.checkSdImageModelTagNameExist(tag)) {
            return ApiResult.fail("修改'" + tag.getTitle() + "'失败，名称已存在");
        }
        return ApiResult.condition(sdImageTagService.updateById(tag));
    }

    /**
     * 设置AI绘画推荐作品标签分类状态
     */
    @Log(value = "AI绘画推荐作品标签分类", exception = "修改AI绘画推荐作品标签分类请求异常")
    @PutMapping("/set-status")
    @Operation(summary = "修改AI绘画推荐作品标签分类状态")
    public ApiResult<?> setSdModelCategoryStatus(@Validated @RequestBody UpdateStatusDTO updateStatusDTO) {
        return ApiResult.condition(sdImageTagService.setSdImageModelTagStatus(updateStatusDTO));
    }

    /**
     * 删除AI绘画推荐作品标签分类
     */
    @Log(value = "AI绘画推荐作品标签分类", exception = "删除AI绘画推荐作品标签分类请求异常")
    @DeleteMapping
    @Parameter(name = "id", description = "AI绘画推荐作品标签分类ID", in = ParameterIn.QUERY)
    @Operation(summary = "删除AI绘画推荐作品标签分类")
    public ApiResult<?> remove(@RequestParam Long id) {
        return ApiResult.condition(sdImageTagService.removeById(id));
    }
}
