package com.auiucloud.component.translate.controller;

import com.auiucloud.component.translate.component.TranslateFactory;
import com.auiucloud.component.translate.component.TranslateService;
import com.auiucloud.component.translate.domain.TextTranslateParams;
import com.auiucloud.core.common.api.ApiResult;
import com.auiucloud.core.log.annotation.Log;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @author dries
 **/
@RestController
@AllArgsConstructor
@Tag(name = "机器翻译")
@RequestMapping("/api/translation")
public class TranslateApiController {

    /**
     * 通用文本翻译
     *
     * @return ApiResult
     */
    @Log(value = "通用文本翻译")
    @Operation(summary = "通用文本翻译")
    @PostMapping("/text")
    public ApiResult<?> textTranslation(@Validated @RequestBody TextTranslateParams params) {
        TranslateService translateService = TranslateFactory.build();
        return translateService.textTranslation(params);
    }

}
