package com.auiucloud.core.security.handle;

import cn.hutool.json.JSONUtil;
import com.auiucloud.core.common.api.ApiResponse;
import com.auiucloud.core.common.api.ResultCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Cache-Control", "no-cache");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        log.error("路径无权限: {}", request.getRequestURL());
        log.error(ex.getMessage());
        response.getWriter().println(JSONUtil.parse(ApiResponse.fail(ResultCode.USER_ERROR_A0230)));
        response.getWriter().flush();
    }
}
