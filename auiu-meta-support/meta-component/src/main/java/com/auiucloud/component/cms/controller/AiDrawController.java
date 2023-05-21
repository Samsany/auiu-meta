package com.auiucloud.component.cms.controller;

import com.auiucloud.component.cms.domain.Gallery;
import com.auiucloud.component.cms.domain.SdText2ImgConfig;
import com.auiucloud.component.cms.service.IAiDrawService;
import com.auiucloud.core.common.api.ApiResult;
import com.auiucloud.core.log.annotation.Log;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author dries
 **/
@Tag(name = "AI绘画API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/ai-draw")
public class AiDrawController {

    private final IAiDrawService aiDrawService;

    @Log(value = "Ai绘画")
    @Operation(summary = "SD文生图")
    @PostMapping(value = "/sd-api/text2img")
    public ApiResult<?> sdText2Img(@RequestBody SdText2ImgConfig config) {
        return aiDrawService.sdText2Img(config);
    }

}
