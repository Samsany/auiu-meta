package com.auiucloud.component.sd.controller;

/**
 * @author dries
 **/

import com.auiucloud.component.sd.service.ISdConfigService;
import com.auiucloud.component.sd.service.ISdDrawCategoryService;
import com.auiucloud.component.sd.service.ISdModelService;
import com.auiucloud.core.common.api.ApiResult;
import com.auiucloud.core.log.annotation.Log;
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

    @Log(value = "Ai绘画菜单")
    @Operation(summary = "Ai绘画菜单")
    @GetMapping(value = "/menu/list")
    public ApiResult<?> aiDrawMenuList() {
        return ApiResult.data(sdDrawCategoryService.aiDrawMenuList());
    }

    @Log(value = "Ai绘画")
    @Operation(summary = "SD配置")
    @GetMapping(value = "/sd-model/list")
    @Parameters({
            @Parameter(name = "cateId", description = "分类ID", in = ParameterIn.QUERY),
    })
    public ApiResult<?> sdDrawConfig(@RequestParam Long cateId) {
        return ApiResult.data(sdModelService.selectSdModelVOListByCateId(cateId));
    }

}
