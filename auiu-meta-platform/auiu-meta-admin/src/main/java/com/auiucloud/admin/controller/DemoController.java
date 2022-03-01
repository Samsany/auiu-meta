package com.auiucloud.admin.controller;

import com.auiucloud.core.common.api.ApiResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
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

    @ApiOperation(value = "获取测试数据")
    @GetMapping("/csData")
    public ApiResponse<String> csData() {
        return ApiResponse.success("Hello World!");
    }

}
