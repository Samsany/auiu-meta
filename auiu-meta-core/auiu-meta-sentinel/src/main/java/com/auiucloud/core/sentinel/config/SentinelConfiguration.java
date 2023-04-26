package com.auiucloud.core.sentinel.config;

import com.alibaba.cloud.sentinel.feign.SentinelFeignAutoConfiguration;
import com.alibaba.csp.sentinel.adapter.spring.webflux.callback.BlockRequestHandler;
import com.alibaba.csp.sentinel.adapter.spring.webmvc.callback.BlockExceptionHandler;
import com.alibaba.fastjson.JSONObject;
import com.auiucloud.core.common.api.ApiResult;
import com.auiucloud.core.sentinel.feign.MateSentinelInvocationHandler;
import feign.Feign;
import feign.InvocationHandlerFactory;
import feign.Target;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerResponse;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * @author dries
 * @date 2021/12/22
 */
@Configuration(proxyBeanMethods = false)
@AutoConfigureBefore(SentinelFeignAutoConfiguration.class)
public class SentinelConfiguration {

    @Bean
    @Scope("prototype")
    @ConditionalOnMissingBean
    @ConditionalOnProperty(name = "spring.cloud.openfeign.sentinel.enabled")
    public Feign.Builder feignSentinelBuilder() {
        return new Feign.Builder();
    }

    /**
     * 限流、熔断统一处理类
     */
    @Configuration
    @ConditionalOnClass(HttpServletRequest.class)
    public static class WebmvcHandler {
        @Bean
        public BlockExceptionHandler webmvcBlockExceptionHandler() {
            return (request, response, e) -> {
                response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
                ApiResult<?> result = ApiResult.fail("Too many request, please retry later.");
                response.getWriter().print(JSONObject.toJSONString(result));
            };
        }

    }

    /**
     * 限流、熔断统一处理类
     */
    @Configuration
    @ConditionalOnClass(ServerResponse.class)
    public static class WebfluxHandler {
        @Bean
        public BlockRequestHandler webfluxBlockExceptionHandler() {
            return (exchange, t) ->
                    ServerResponse.status(HttpStatus.TOO_MANY_REQUESTS)
                            .contentType(MediaType.APPLICATION_JSON)
                            .body(BodyInserters.fromValue(ApiResult.fail(t.getMessage())));
        }
    }

}
