package com.auiucloud.admin.controller;

import com.auiucloud.core.common.api.ApiResult;
import com.auiucloud.core.database.model.Search;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
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
    @ApiImplicitParams({
            @ApiImplicitParam(name = "queryMode", value = "查询模式", paramType = "query"),
            @ApiImplicitParam(name = "pageNum", value = "当前页", paramType = "query"),
            @ApiImplicitParam(name = "pageSize", value = "每页显示数据", paramType = "query"),
            @ApiImplicitParam(name = "keyword",  value = "模糊查询关键词", paramType = "query"),
            @ApiImplicitParam(name = "startDate", value = "创建开始日期", paramType = "query"),
            @ApiImplicitParam(name = "endDate", value = "创建结束日期", paramType = "query"),
    })
    @GetMapping("/csData")
    public ApiResult<String> csData(Search search) {
        return ApiResult.success("Hello World!");
    }

}
