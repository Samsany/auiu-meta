package com.auiucloud.core.security.handle;

import com.auiucloud.core.common.api.ApiResult;
import com.auiucloud.core.common.api.ResultCode;
import com.auiucloud.core.common.utils.http.ResponseUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;

/**
 * 自定义返回结果：未登录或登录过期
 *
 * @author dries
 * @date 2021/12/22
 */
@Slf4j
public class RestAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException ex) throws IOException, ServletException {
        log.error("处理未认证: {}", request.getRequestURL());
        log.error(ex.getMessage());
        ResponseUtil.responseWriter(response, MediaType.APPLICATION_JSON_VALUE, HttpStatus.OK.value(), ApiResult.fail(ResultCode.USER_ERROR_A0230));
    }
}
