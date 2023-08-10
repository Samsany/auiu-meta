package com.auiucloud.component.sd.controller;

/**
 * @author dries
 **/

import com.auiucloud.component.sd.domain.SdImageTag;
import com.auiucloud.component.sd.service.ISdDrawCategoryService;
import com.auiucloud.component.sd.service.ISdImageModelService;
import com.auiucloud.component.sd.service.ISdImageTagService;
import com.auiucloud.component.sd.service.ISdModelService;
import com.auiucloud.core.common.api.ApiResult;
import com.auiucloud.core.common.constant.CommonConstant;
import com.auiucloud.core.database.model.Search;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author dries
 **/
@Tag(name = "SD开放API")
@RestController
@AllArgsConstructor
@RequestMapping("/open-api/ai-draw")
public class AiDrawOpenApiController {

    private final ISdDrawCategoryService sdDrawCategoryService;
    private final ISdModelService sdModelService;
    private final ISdImageTagService sdImageTagService;
    private final ISdImageModelService sdImageModelService;


    @Operation(summary = "Ai绘画菜单")
    @GetMapping(value = "/menu/list")
    public ApiResult<?> aiDrawMenuList() {
        return ApiResult.data(sdDrawCategoryService.aiDrawMenuList());
    }

    @Operation(summary = "SD配置")
    @GetMapping(value = "/sd-model/list")
    @Parameters({
            @Parameter(name = "cateId", description = "分类ID", in = ParameterIn.QUERY),
    })
    public ApiResult<?> sdDrawConfig(@RequestParam Long cateId) {
        return ApiResult.data(sdModelService.selectSdModelVOListByCateId(cateId));
    }

    @Operation(summary = "AI绘画作品标签列表")
    @GetMapping(value = "/sd-image/tags")
    public ApiResult<?> sdImageTagList() {
        return ApiResult.data(sdImageTagService.list(Wrappers.<SdImageTag>lambdaQuery()
                .eq(SdImageTag::getStatus, CommonConstant.STATUS_NORMAL_VALUE)
                .orderByDesc(SdImageTag::getSort)
        ));
    }

    @Operation(summary = "AI绘画作品分页列表")
    @GetMapping(value = "/sd-image/page")
    @Parameters({
            @Parameter(name = "tags", description = "标签ID", in = ParameterIn.QUERY),
    })
    public ApiResult<?> sdImageModelPage(@Parameter(hidden = true) Search search, @RequestParam(required = false, defaultValue = "4,5133") String tags) {
        return ApiResult.data(sdImageModelService.selectSdImageModelPage(search, tags));
    }

}
