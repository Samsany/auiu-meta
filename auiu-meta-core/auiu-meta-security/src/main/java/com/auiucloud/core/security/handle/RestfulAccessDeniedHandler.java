package com.auiucloud.core.security.handle;

import cn.hutool.json.JSONUtil;
import com.auiucloud.core.common.api.ApiResponse;
import com.auiucloud.core.common.api.ResultCode;
import lombok.extern.slf4j.Slf4j;
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
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Cache-Control", "no-cache");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        log.error("用户未授权操作: {}", ex.getMessage());
        response.getWriter().write(JSONUtil.toJsonStr(ApiResponse.fail(ResultCode.USER_ERROR_A0301)));
        response.getWriter().flush();
    }
}
