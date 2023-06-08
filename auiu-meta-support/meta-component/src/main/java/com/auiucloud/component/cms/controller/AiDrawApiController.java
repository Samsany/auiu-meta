package com.auiucloud.component.cms.controller;

import com.auiucloud.component.cms.service.IAiDrawService;
import com.auiucloud.component.cms.service.IGalleryService;
import com.auiucloud.component.cms.vo.SdDrawParamVO;
import com.auiucloud.component.cms.vo.SdDrawSaveVO;
import com.auiucloud.core.common.api.ApiResult;
import com.auiucloud.core.log.annotation.Log;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @author dries
 **/
@Tag(name = "AI绘画API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/cms/ai-draw")
public class AiDrawApiController {

    private final IAiDrawService aiDrawService;
    private final IGalleryService galleryService;

    @Log(value = "Ai绘画")
    @Operation(summary = "SD文生图")
    @PostMapping(value = "/sd-api/text2img")
    public ApiResult<?> sdText2Img(@Validated @RequestBody SdDrawParamVO drawParamVO) {
        return aiDrawService.sdText2Img(drawParamVO);
    }

    @Log(value = "Ai绘画")
    @Operation(summary = "保存创作作品")
    @PostMapping(value = "/save")
    public ApiResult<?> saveDraw(@Validated @RequestBody SdDrawSaveVO vo) {
        return galleryService.saveSdDrawGallery(vo);
    }

}
