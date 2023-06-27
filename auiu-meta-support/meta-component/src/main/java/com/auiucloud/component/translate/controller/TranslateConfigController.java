package com.auiucloud.component.translate.controller;

import com.auiucloud.component.translate.domain.TranslateProperties;
import com.auiucloud.component.translate.service.ITranslateConfigService;
import com.auiucloud.core.common.api.ApiResult;
import com.auiucloud.core.log.annotation.Log;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @author dries
 **/
@RestController
@AllArgsConstructor
@Tag(name = "翻译配置管理")
@RequestMapping("/translation/config")
public class TranslateConfigController {

    private final ITranslateConfigService translationService;

    /**
     * 获取SD配置列表
     *
     * @return ApiResult
     */
    @Log(value = "查询SD配置列表")
    @Operation(summary = "查询SD配置列表")
    @GetMapping("/list")
    public ApiResult<?> list() {
        return ApiResult.data(translationService.selectTranslationConfigList());
    }

    /**
     * 查询SD配置
     *
     * @param configCode 　代码
     * @return ApiResult
     */
    @Log(value = "查询SD配置")
    @GetMapping("/get-config-by-code")
    @Operation(summary = "查询SD配置")
    public ApiResult<?> getConfigByCode(@RequestParam String configCode) {
        return ApiResult.data(translationService.getTranslationConfigByCode(configCode));
    }


    /**
     * 保存翻译配置
     *
     * @param properties 　翻译配置
     * @return ApiResult
     */
    @Log(value = "保存翻译配置")
    @Operation(summary = "保存翻译配置")
    @PostMapping("/save")
    public ApiResult<?> saveTranslationConfig(@Valid @RequestBody TranslateProperties properties) {
        return ApiResult.condition(translationService.saveConfigTranslation(properties));
    }

}
