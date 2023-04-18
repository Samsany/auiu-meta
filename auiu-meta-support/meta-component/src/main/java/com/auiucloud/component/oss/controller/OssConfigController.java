package com.auiucloud.component.oss.controller;

import cn.hutool.core.util.StrUtil;
import com.auiucloud.component.sysconfig.service.ISysConfigService;
import com.auiucloud.core.common.api.ApiResult;
import com.auiucloud.core.log.annotation.Log;
import com.auiucloud.core.oss.props.OssProperties;
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
@Tag(name = "OSS配置管理")
@RequestMapping("/oss/config")
public class OssConfigController {

    private final ISysConfigService sysConfigService;

    /**
     * 查询OSS配置
     *
     * @param configCode 　代码
     * @return ApiResult
     */
    @Log(value = "查询OSS配置")
    @GetMapping("/get-config-by-code")
    @Operation(summary = "查询OSS配置")
    public ApiResult<?> getConfigByCode(@RequestParam String configCode) {
        return ApiResult.data(sysConfigService.getConfigByCode(configCode));
    }

    /**
     * 默认配置
     *
     * @return ApiResult
     */
    @Log(value = "默认配置")
    @Operation(summary = "默认配置")
    @GetMapping("/default-oss")
    public ApiResult<?> defaultOss() {
        OssProperties oss = sysConfigService.getOssProperties();
        // 对oss部分字段进行隐藏显示，保护隐私
        oss.setSecretKey(StrUtil.hide(oss.getSecretKey(), 3, 23));
        return ApiResult.data(oss);
    }

    /**
     * 保存OSS配置
     *
     * @param ossProperties 　oss配置
     * @return
     */
    @Log(value = "保存OSS配置")
    @Operation(summary = "保存OSS配置")
    @PostMapping("/save-config-oss")
    public ApiResult<?> saveConfigOss(@Valid @RequestBody OssProperties ossProperties) {
        return ApiResult.condition(sysConfigService.saveConfigOss(ossProperties));
    }

}
