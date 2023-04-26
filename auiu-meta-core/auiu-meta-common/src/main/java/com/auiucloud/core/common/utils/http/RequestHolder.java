package com.auiucloud.core.common.utils.http;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Objects;

/**
 * 获取HttpServletRequest
 *
 * @author dries
 * @date 2021/12/20
 */
public class RequestHolder {

    /**
     * 获取HttpServletRequest请求
     *
     * @return HttpServletRequest
     */
    public static HttpServletRequest getHttpServletRequest() {
        return ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
    }

    public static String getHttpServletRequestHeader(String key) {
        HttpServletRequest request = getHttpServletRequest();
        return request.getHeader(key);
    }

}
