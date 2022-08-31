package com.auiucloud.gateway.handler;

import com.auiucloud.core.common.api.ApiResult;
import com.auiucloud.core.common.api.ResultCode;
import com.auiucloud.core.common.utils.ResponseUtil;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.cloud.gateway.support.NotFoundException;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * 网关统一异常拦截
 *
 * @author matevip
 */
@Slf4j
@Order(-1)
@Configuration
public class ExceptionHandler implements ErrorWebExceptionHandler {

    @NotNull
    @Override
    public Mono<Void> handle(ServerWebExchange exchange, @NotNull Throwable ex) {
        ServerHttpResponse response = exchange.getResponse();

        if (exchange.getResponse().isCommitted()) {
            return Mono.error(ex);
        }

        String msg;
        if (ex instanceof NotFoundException) {
            msg = "服务未找到";
        } else if (ex instanceof ResponseStatusException) {
            ResponseStatusException responseStatusException = (ResponseStatusException) ex;
            msg = responseStatusException.getMessage();
        } else {
            msg = "内部服务器错误";
        }
        log.error("[网关异常处理]请求路径:{},异常信息:{}", exchange.getRequest().getPath(), ex.getMessage());

        ApiResult<?> result = ApiResult.fail(ResultCode.USER_ERROR_A0500.getCode(), msg);
        return ResponseUtil.webFluxResponseWriter(response, MediaType.APPLICATION_JSON_VALUE, HttpStatus.OK, result);
    }

}
