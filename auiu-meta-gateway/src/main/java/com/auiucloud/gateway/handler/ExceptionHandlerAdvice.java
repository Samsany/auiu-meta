package com.auiucloud.gateway.handler;

import cn.hutool.core.util.StrUtil;
import com.auiucloud.core.common.api.ApiResult;
import com.auiucloud.core.common.api.ResultCode;
import com.auiucloud.core.common.exception.TokenException;
import com.auiucloud.core.common.utils.StringPool;
import io.netty.channel.ConnectTimeoutException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.support.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.server.ResponseStatusException;

/**
 * 异常处理通知
 *
 * @author dries
 * @date 2021/12/27
 */
@Slf4j
@Component
public class ExceptionHandlerAdvice {

    @ExceptionHandler(value = {ResponseStatusException.class})
    public ApiResult<?> handle(ResponseStatusException ex) {
        log.error("response status exception:{}", ex.getMessage());
        if (StrUtil.contains(ex.getMessage(), HttpStatus.NOT_FOUND.toString())) {
            return ApiResult.fail(ResultCode.NOT_FOUND, ex.getMessage());
        } else if (StrUtil.contains(ex.getMessage(), HttpStatus.SERVICE_UNAVAILABLE.toString())) {
            // 状态为503时，打印消息
            return ApiResult.fail(ResultCode.ERROR,
                    ResultCode.ERROR.getMessage() + StringPool.COLON + ex.getMessage());
        } else {
            return ApiResult.fail(ResultCode.ERROR);
        }
    }

    @ExceptionHandler(value = {ConnectTimeoutException.class})
    public ApiResult<?> handle(ConnectTimeoutException ex) {
        log.error("connect timeout exception:{}", ex.getMessage());
        return ApiResult.fail(ResultCode.ERROR);
    }

    @ExceptionHandler(value = {NotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiResult<?> handle(NotFoundException ex) {
        log.error("not found exception:{}", ex.getMessage());
        return ApiResult.fail(ResultCode.NOT_FOUND);
    }

    @ExceptionHandler(value = {RuntimeException.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiResult<?> handle(RuntimeException ex) {
        log.error("runtime exception:{}", ex.getMessage());
        return ApiResult.fail(ex.getMessage());
    }

    @ExceptionHandler(value = {TokenException.class})
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ApiResult<?> handle(TokenException ex) {
        log.error("runtime exception:{}", ex.getMessage());
        return ApiResult.fail(HttpStatus.UNAUTHORIZED.value(), ex.getMessage());
    }

    @ExceptionHandler(value = {Exception.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiResult<?> handle(Exception ex) {
        log.error("exception:{}", ex.getMessage());
        return ApiResult.fail();
    }

    @ExceptionHandler(value = {Throwable.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiResult<?> handle(Throwable throwable) {
        ApiResult<?> apiResult = ApiResult.fail();
        if (throwable instanceof ResponseStatusException) {
            apiResult = handle((ResponseStatusException) throwable);
        } else if (throwable instanceof ConnectTimeoutException) {
            apiResult = handle((ConnectTimeoutException) throwable);
        } else if (throwable instanceof NotFoundException) {
            apiResult = handle((NotFoundException) throwable);
        } else if (throwable instanceof TokenException) {
            apiResult = handle((TokenException) throwable);
        } else if (throwable instanceof RuntimeException) {
            apiResult = handle((RuntimeException) throwable);
        } else if (throwable instanceof Exception) {
            apiResult = handle((Exception) throwable);
        }
        return apiResult;
    }
}
