package com.auiucloud.admin.controller;

import cn.hutool.json.JSONUtil;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingFactory;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.pojo.Instance;
import com.auiucloud.core.common.api.ApiResult;
import com.auiucloud.core.log.annotation.Log;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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

    /**
     * 获取全部nacos服务实例
     */
    @Log(value = "公共模块", exception = "获取全部nacos服务实例请求异常")
    @ApiOperation("获取全部nacos服务实例")
    @GetMapping("/nacos/getAllInstances")
    public ApiResult<?> getAllInstances() throws NacosException {
        NamingService namingService = NamingFactory.createNamingService(nacosServerAddr);
        List<Instance> allInstances = namingService.getAllInstances("nacos.myxxiaxiang.cn");
        log.error(JSONUtil.toJsonStr(allInstances));
        return ApiResult.data(allInstances);
    }

}
