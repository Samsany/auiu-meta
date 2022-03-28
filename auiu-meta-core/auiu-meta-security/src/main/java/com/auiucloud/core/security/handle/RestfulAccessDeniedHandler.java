package com.auiucloud.core.security.handle;

import com.auiucloud.core.common.api.ApiResult;
import com.auiucloud.core.common.api.ResultCode;
import com.auiucloud.core.common.utils.ResponseUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 自定义返回结果：没有权限访问时
 *
 * @author dries
 * @date 2021/12/22
 */
@Slf4j
public class RestfulAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException ex) throws IOException, ServletException {
        log.error("处理未授权: {}", ex.getMessage());
        ResponseUtil.responseWriter(response, MediaType.APPLICATION_JSON_VALUE, HttpStatus.OK.value(), ApiResult.fail(ResultCode.USER_ERROR_A0301));
    }
}
