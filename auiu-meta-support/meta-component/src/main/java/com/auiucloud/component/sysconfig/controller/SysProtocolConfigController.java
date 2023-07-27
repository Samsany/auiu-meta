package com.auiucloud.component.sysconfig.controller;

import com.auiucloud.component.sysconfig.domain.SysProtocolConfig;
import com.auiucloud.component.sysconfig.service.ISysProtocolConfigService;
import com.auiucloud.core.common.api.ApiResult;
import com.auiucloud.core.common.model.dto.UpdateStatusDTO;
import com.auiucloud.core.database.model.Search;
import com.auiucloud.core.log.annotation.Log;
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
@RestController
@AllArgsConstructor
@Tag(name = "基础配置管理")
@RequestMapping("/system/protocol/config")
public class SysProtocolConfigController {

    private final ISysProtocolConfigService sysProtocolConfigService;

    /**
     * 查询系统协议配置列表
     */
    @Log(value = "系统协议配置", exception = "查询系统协议配置列表请求异常")
    @GetMapping("/list")
    @Operation(summary = "查询图片标签列表")
    @Parameters({
            @Parameter(name = "pageNum", description = "当前页", in = ParameterIn.QUERY),
            @Parameter(name = "pageSize", description = "每页显示数据", in = ParameterIn.QUERY),
            @Parameter(name = "keyword", description = "关键字", in = ParameterIn.QUERY),
    })
    public ApiResult<?> list(Search search, @Parameter(hidden = true) SysProtocolConfig protocolConfig) {
        return ApiResult.data(sysProtocolConfigService.listPage(search, protocolConfig));
    }

    /**
     * 获取系统协议配置详情
     */
    @Log(value = "系统协议配置", exception = "获取系统协议配置详情请求异常")
    @GetMapping(value = "/{id}")
    @Operation(summary = "获取系统协议配置详情", description = "根据id获取系统协议配置详情")
    @Parameters({
            @Parameter(name = "id", required = true, description = "ID", in = ParameterIn.PATH),
    })
    public ApiResult<?> getInfo(@PathVariable("id") Long id) {
        return ApiResult.data(sysProtocolConfigService.getById(id));
    }

    /**
     * 新增系统协议配置
     */
    @Log(value = "系统协议配置", exception = "新增系统协议配置请求异常")
    @PostMapping
    @Operation(summary = "新增系统协议配置")
    public ApiResult<?> add(@RequestBody SysProtocolConfig protocolConfig) {
        if (sysProtocolConfigService.checkProtocolConfigNameExist(protocolConfig)) {
            return ApiResult.fail("新增标签'" + protocolConfig.getTitle() + "'失败，名称已存在");
        }
        return ApiResult.condition(sysProtocolConfigService.save(protocolConfig));
    }

    /**
     * 修改系统协议配置
     */
    @Log(value = "系统协议配置", exception = "修改系统协议配置请求异常")
    @PutMapping
    @Operation(summary = "修改系统协议配置")
    public ApiResult<?> edit(@RequestBody SysProtocolConfig protocolConfig) {
        if (sysProtocolConfigService.checkProtocolConfigNameExist(protocolConfig)) {
            return ApiResult.fail("修改'" + protocolConfig.getTitle() + "'失败，名称已存在");
        }
        return ApiResult.condition(sysProtocolConfigService.updateSysProtocolConfigById(protocolConfig));
    }

    /**
     * 设置系统协议配置状态
     */
    @Log(value = "系统协议配置", exception = "修改图片标签请求异常")
    @PutMapping("/setStatus")
    @Operation(summary = "修改系统协议配置状态")
    public ApiResult<?> setStatus(@Validated @RequestBody UpdateStatusDTO updateStatusDTO) {
        return ApiResult.condition(sysProtocolConfigService.setStatus(updateStatusDTO));
    }

    /**
     * 删除系统协议配置
     */
    @Log(value = "系统协议配置", exception = "删除系统协议配置请求异常")
    @DeleteMapping("/delete/{picTagId}")
    @Parameter(name = "picTagId", description = "系统协议配置ID", in = ParameterIn.PATH)
    @Operation(summary = "删除系统协议配置")
    public ApiResult<?> remove(@PathVariable Long picTagId) {
        return ApiResult.condition(sysProtocolConfigService.removeById(picTagId));
    }

}
