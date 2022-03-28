package com.auiucloud.core.common.utils;

import cn.hutool.json.JSONUtil;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import reactor.core.publisher.Mono;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * 响应工具
 *
 * @author dries
 * @date 2021/12/20
 */
public class ResponseUtil {

    /**
     * 设置响应
     *
     * @param response    HttpServletResponse
     * @param contentType content-type
     * @param status      http状态码
     * @param value       响应内容
     * @throws IOException IOException
     */
    public static void responseWriter(HttpServletResponse response, String contentType,
                                      int status, Object value) throws IOException {
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Cache-Control", "no-cache");
        response.setCharacterEncoding(StandardCharsets.UTF_8.toString());
        response.setContentType(contentType);
        response.setStatus(status);
        response.getWriter().write(JSONUtil.toJsonStr(value));
        response.getWriter().flush();
    }

    /**
     * 设置webflux模型响应
     *
     * @param response    ServerHttpResponse
     * @param contentType content-type
     * @param status      http状态码
     * @param value       响应内容
     * @return Mono<Void>
     */
    public static Mono<Void> webFluxResponseWriter(ServerHttpResponse response, String contentType,
                                                   HttpStatus status, Object value) {
        response.setStatusCode(status);
        response.getHeaders().add(HttpHeaders.CONTENT_TYPE, contentType);
        DataBuffer dataBuffer = response.bufferFactory().wrap(JSONUtil.toJsonStr(value).getBytes());
        return response.writeWith(Mono.just(dataBuffer));
    }

}
