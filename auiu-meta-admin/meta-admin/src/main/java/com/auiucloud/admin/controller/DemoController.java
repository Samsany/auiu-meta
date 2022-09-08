package com.auiucloud.admin.controller;

import com.auiucloud.core.common.api.ApiResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author dries
 * @date 2022/1/9
 */
@Slf4j
@Api(tags = "demo")
@RestController
public class DemoController {

    @Value("spring.cloud.nacos.config.server-addr")
    private String nacosServerAddr;

    @ApiOperation(value = "获取测试数据")
    @GetMapping("/csData")
    public ApiResult<String> csData() {
        return ApiResult.success("Hello World!");
    }

}
