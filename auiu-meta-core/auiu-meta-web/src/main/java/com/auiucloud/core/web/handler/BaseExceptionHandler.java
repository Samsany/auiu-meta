package com.auiucloud.core.web.handler;

import com.auiucloud.core.common.api.ApiResult;
import com.auiucloud.core.common.api.ResultCode;
import com.auiucloud.core.common.exception.ApiException;
import com.auiucloud.core.common.exception.TokenException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.io.FileNotFoundException;

/**
 * 全局异常处理
 *
 * @author dries
 * @date 2021/12/22
 */
@Slf4j
@RestControllerAdvice
public class BaseExceptionHandler {

    /**
     * BaseException 异常捕获处理
     *
     * @param ex 自定义BaseException异常类型
     * @return Result
     */
    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiResult<?> handleException(ApiException ex) {
        log.error("程序异常：" + ex.toString());
        if (ex.getResultCode() != null) {
            return ApiResult.fail(ex.getResultCode());
        }
        return ApiResult.fail(ex.getMessage());
    }

    /**
     * TokenException 异常捕获处理
     *
     * @param ex 自定义TokenException异常类型
     * @return Result
     */
    @ExceptionHandler
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ApiResult<?> handleException(TokenException ex) {
        log.error("程序异常==>errorCode:{}, exception:{}", HttpStatus.UNAUTHORIZED.value(), ex.getMessage());
        return ApiResult.fail(ResultCode.USER_ERROR_A0230);
    }

    /**
     * FileNotFoundException,NoHandlerFoundException 异常捕获处理
     *
     * @param exception 自定义FileNotFoundException异常类型
     * @return Result
     */
    @ExceptionHandler({FileNotFoundException.class, NoHandlerFoundException.class})
    public ApiResult<?> noFoundException(Exception exception) {
        log.error("程序异常==>errorCode:{}, exception:{}", HttpStatus.NOT_FOUND.value(), exception.getMessage());
        return ApiResult.fail(HttpStatus.NOT_FOUND.value(), exception.getMessage());
    }

    /**
     * NullPointerException 空指针异常捕获处理
     *
     * @param ex 自定义NullPointerException异常类型
     * @return Result
     */
    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiResult<?> handleException(NullPointerException ex) {
        log.error("程序异常：{}" + ex.toString());
        return ApiResult.fail(HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage());
    }

    /**
     * 通用Exception异常捕获
     *
     * @param ex 自定义Exception异常类型
     * @return Result
     */
    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiResult<?> handleException(Exception ex) {
        log.error("程序异常：" + ex.toString());
        String message = ex.getMessage();
        if (StringUtils.contains(message, "Bad credentials")) {
            message = "您输入的密码不正确";
        } else if (StringUtils.contains(ex.toString(), "InternalAuthenticationServiceException")) {
            message = "内部认证服务异常";
        }
        return ApiResult.fail(HttpStatus.INTERNAL_SERVER_ERROR.value(), message);
    }

}
