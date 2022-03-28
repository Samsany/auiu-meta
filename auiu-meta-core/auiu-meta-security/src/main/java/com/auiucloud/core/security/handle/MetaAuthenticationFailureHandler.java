package com.auiucloud.core.security.handle;

import com.auiucloud.core.common.api.ApiResult;
import com.auiucloud.core.common.api.ResultCode;
import com.auiucloud.core.common.utils.ResponseUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.*;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author dries
 * @date 2021/12/22
 */
@Slf4j
public class MetaAuthenticationFailureHandler implements AuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        ApiResult<?> apiResult = null;
        String username = request.getParameter("username");
        if (exception instanceof AccountExpiredException) {
            // 账号过期
            log.info("[登录失败] - 用户[{}]账号过期", username);
            apiResult = ApiResult.fail(ResultCode.USER_ERROR_A0205);

        } else if (exception instanceof BadCredentialsException) {
            // 密码错误
            log.info("[登录失败] - 用户[{}]密码错误", username);
            apiResult = ApiResult.fail(ResultCode.USER_ERROR_A0210);

        } else if (exception instanceof CredentialsExpiredException) {
            // 密码过期
            log.info("[登录失败] - 用户[{}]密码过期", username);
            apiResult = ApiResult.fail(ResultCode.USER_ERROR_A0212);

        } else if (exception instanceof DisabledException) {
            // 用户被禁用
            log.info("[登录失败] - 用户[{}]被禁用", username);
            apiResult = ApiResult.fail(ResultCode.USER_ERROR_A0202);

        } else if (exception instanceof LockedException) {
            // 用户被锁定
            log.info("[登录失败] - 用户[{}]被锁定", username);
            apiResult = ApiResult.fail(ResultCode.USER_ERROR_A0204);

        } else if (exception instanceof InternalAuthenticationServiceException) {
            // 内部错误
            log.error(String.format("[登录失败] - [%s]内部错误", username));
            apiResult = ApiResult.fail(ResultCode.USER_ERROR_A0200);

        } else {
            // 其他错误
            log.error(String.format("[登录失败] - [%s]其他错误", username), exception);
            apiResult = ApiResult.fail(ResultCode.USER_ERROR_A0200);
        }
        ResponseUtil.responseWriter(response, "UTF-8", HttpStatus.UNAUTHORIZED.value(), apiResult);
    }
}
