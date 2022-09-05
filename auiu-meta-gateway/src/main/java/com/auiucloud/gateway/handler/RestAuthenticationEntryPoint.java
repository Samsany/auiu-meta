package com.auiucloud.gateway.handler;

import com.auiucloud.core.common.api.ApiResult;
import com.auiucloud.core.common.api.ResultCode;
import com.auiucloud.core.common.utils.http.ResponseUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.server.ServerAuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * 自定义返回结果：未登录或登录过期
 *
 * @author dries
 * @date 2021/12/22
 */
@Slf4j
@Component
public class RestAuthenticationEntryPoint implements ServerAuthenticationEntryPoint {

    @Override
    public Mono<Void> commence(ServerWebExchange exchange, AuthenticationException ex) {
        log.error("处理未认证: {}", exchange.getRequest().getURI());
        log.error(ex.getMessage());
        ServerHttpResponse response = exchange.getResponse();
        return ResponseUtil.webFluxResponseWriter(response, MediaType.APPLICATION_JSON_VALUE, HttpStatus.OK, ApiResult.fail(ResultCode.USER_ERROR_A0230));
    }
}
