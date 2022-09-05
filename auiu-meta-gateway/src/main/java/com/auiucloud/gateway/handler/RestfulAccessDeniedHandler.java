package com.auiucloud.gateway.handler;

import com.auiucloud.core.common.api.ApiResult;
import com.auiucloud.core.common.api.ResultCode;
import com.auiucloud.core.common.utils.http.ResponseUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.server.authorization.ServerAccessDeniedHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * 自定义返回结果：没有权限访问时
 *
 * @author dries
 * @date 2021/12/22
 */
@Slf4j
@Component
public class RestfulAccessDeniedHandler implements ServerAccessDeniedHandler {

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, AccessDeniedException denied) {
        log.error("处理未授权: {}", denied.getMessage());
        ServerHttpResponse response = exchange.getResponse();
        return ResponseUtil.webFluxResponseWriter(response, MediaType.APPLICATION_JSON_VALUE, HttpStatus.OK, ApiResult.fail(ResultCode.USER_ERROR_A0301));
    }

}
