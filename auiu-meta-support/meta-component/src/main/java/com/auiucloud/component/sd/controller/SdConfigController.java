package com.auiucloud.component.sd.controller;

import com.auiucloud.component.sd.domain.SdConfigProperties;
import com.auiucloud.component.sd.service.ISdConfigService;
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
@Tag(name = "SD配置管理")
@RequestMapping("/sd/config")
public class SdConfigController {

    private final ISdConfigService sdConfigService;

    /**
     * 获取SD配置列表
     *
     * @return ApiResult
     */
    @Operation(summary = "查询SD配置列表")
    @GetMapping("/list")
    public ApiResult<?> list() {
        return ApiResult.data(sdConfigService.selectSdConfigList());
    }

    /**
     * 查询SD配置
     *
     * @param configCode 　代码
     * @return ApiResult
     */
    @GetMapping("/get-config-by-code")
    @Operation(summary = "查询SD配置")
    public ApiResult<?> getConfigByCode(@RequestParam String configCode) {
        return ApiResult.data(sdConfigService.getConfigByCode(configCode));
    }

    /**
     * 保存SD配置
     *
     * @param properties 　SD配置
     * @return ApiResult
     */
    @Log(value = "保存SD配置")
    @Operation(summary = "保存SD配置")
    @PostMapping("/save")
    public ApiResult<?> saveSdConfig(@Valid @RequestBody SdConfigProperties properties) {
        return ApiResult.condition(sdConfigService.saveSdConfig(properties));
    }

}
