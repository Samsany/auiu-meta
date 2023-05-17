package com.auiucloud.admin.modules.system.controller;

import com.auiucloud.core.common.api.ApiResult;
import com.auiucloud.core.database.model.Search;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author dries
 * @date 2022/1/9
 */
@Slf4j
@Tag(name = "demo")
@RestController
public class DemoController {

    @Value("spring.cloud.nacos.config.server-addr")
    private String nacosServerAddr;

    @Operation(summary = "获取测试数据")
    @Parameters({
            @Parameter(name = "queryMode", description = "查询模式", in = ParameterIn.QUERY),
            @Parameter(name = "pageNum", description = "当前页", in = ParameterIn.QUERY),
            @Parameter(name = "pageSize", description = "每页显示数据", in = ParameterIn.QUERY),
            @Parameter(name = "keyword", description = "模糊查询关键词", in = ParameterIn.QUERY),
            @Parameter(name = "startDate", description = "创建开始日期", in = ParameterIn.QUERY),
            @Parameter(name = "endDate", description = "创建结束日期", in = ParameterIn.QUERY),
    })
    @GetMapping("/csData")
    public ApiResult<String> csData(Search search) {
        return ApiResult.success("Hello World!");
    }

}
