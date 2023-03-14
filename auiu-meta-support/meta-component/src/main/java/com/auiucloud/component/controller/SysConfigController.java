package com.auiucloud.component.controller;

import com.auiucloud.component.domain.SysConfig;
import com.auiucloud.component.service.ISysConfigService;
import com.auiucloud.core.common.api.ApiResult;
import com.auiucloud.core.common.enums.QueryModeEnum;
import com.auiucloud.core.database.model.Search;
import com.auiucloud.core.database.utils.PageUtils;
import com.auiucloud.core.log.annotation.Log;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author dries
 **/
@RestController
@AllArgsConstructor
@Tag(name = "基础配置管理")
@RequestMapping("/system/config")
public class SysConfigController {

    private final ISysConfigService configService;

    /**
     * 查询参数配置列表
     */
    @Log(value = "系统参数配置")
    @GetMapping("/default-list")
    @Operation(summary = "查询默认参数配置列表")
    public ApiResult<?> defaultList() {
        return ApiResult.data(configService.selectDefaultConfigList());
    }

    /**
     * 查询参数配置列表
     */
    @Log(value = "系统参数配置", exception = "查询系统参数配置列表请求异常")
    @GetMapping("/list")
    @Operation(summary = "查询参数配置列表")
    @Parameters({
            @Parameter(name = "queryMode", description = "查询模式", in = ParameterIn.QUERY),
            @Parameter(name = "pageNum", description = "当前页", in = ParameterIn.QUERY),
            @Parameter(name = "pageSize", description = "每页显示数据", in = ParameterIn.QUERY),
            @Parameter(name = "configName", description = "配置名称", in = ParameterIn.QUERY)
    })
    public ApiResult<?> list(Search search, @Parameter(hidden = true) SysConfig config) {
        QueryModeEnum mode = QueryModeEnum.getQueryModeByCode(search.getQueryMode());
        switch (mode) {
            case LIST:
                return ApiResult.data(configService.selectConfigList(config));
            case PAGE:
            default:
                PageUtils list = configService.listPage(search, config);
                return ApiResult.data(list);
        }
    }

}
