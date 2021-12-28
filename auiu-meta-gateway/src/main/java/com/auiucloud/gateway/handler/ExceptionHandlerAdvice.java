package com.auiucloud.gateway.handler;

import cn.hutool.core.util.StrUtil;
import com.auiucloud.core.common.api.ApiResponse;
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
    public ApiResponse<?> handle(ResponseStatusException ex) {
        log.error("response status exception:{}", ex.getMessage());
        if (StrUtil.contains(ex.getMessage(), HttpStatus.NOT_FOUND.toString())) {
            return ApiResponse.fail(ResultCode.NOT_FOUND, ex.getMessage());
        } else if (StrUtil.contains(ex.getMessage(), HttpStatus.SERVICE_UNAVAILABLE.toString())) {
            // 状态为503时，打印消息
            return ApiResponse.fail(ResultCode.ERROR,
                    ResultCode.ERROR.getMessage() + StringPool.COLON + ex.getMessage());
        } else {
            return ApiResponse.fail(ResultCode.ERROR);
        }
    }

    @ExceptionHandler(value = {ConnectTimeoutException.class})
    public ApiResponse<?> handle(ConnectTimeoutException ex) {
        log.error("connect timeout exception:{}", ex.getMessage());
        return ApiResponse.fail(ResultCode.ERROR);
    }

    @ExceptionHandler(value = {NotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiResponse<?> handle(NotFoundException ex) {
        log.error("not found exception:{}", ex.getMessage());
        return ApiResponse.fail(ResultCode.NOT_FOUND);
    }

    @ExceptionHandler(value = {RuntimeException.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiResponse<?> handle(RuntimeException ex) {
        log.error("runtime exception:{}", ex.getMessage());
        return ApiResponse.fail(ex.getMessage());
    }

    @ExceptionHandler(value = {TokenException.class})
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ApiResponse<?> handle(TokenException ex) {
        log.error("runtime exception:{}", ex.getMessage());
        return ApiResponse.fail(HttpStatus.UNAUTHORIZED.value(), ex.getMessage());
    }

    @ExceptionHandler(value = {Exception.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiResponse<?> handle(Exception ex) {
        log.error("exception:{}", ex.getMessage());
        return ApiResponse.fail();
    }

    @ExceptionHandler(value = {Throwable.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiResponse<?> handle(Throwable throwable) {
        ApiResponse<?> apiResponse = ApiResponse.fail();
        if (throwable instanceof ResponseStatusException) {
            apiResponse = handle((ResponseStatusException) throwable);
        } else if (throwable instanceof ConnectTimeoutException) {
            apiResponse = handle((ConnectTimeoutException) throwable);
        } else if (throwable instanceof NotFoundException) {
            apiResponse = handle((NotFoundException) throwable);
        } else if (throwable instanceof TokenException) {
            apiResponse = handle((TokenException) throwable);
        } else if (throwable instanceof RuntimeException) {
            apiResponse = handle((RuntimeException) throwable);
        } else if (throwable instanceof Exception) {
            apiResponse = handle((Exception) throwable);
        }
        return apiResponse;
    }
}
