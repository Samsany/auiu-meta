package com.auiucloud.core.feign.config;

import com.auiucloud.core.common.utils.RequestHolder;
import feign.RequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;

/**
 * feign拦截器
 *
 * @author dries
 * @date 2021/12/22
 */
@Configuration
public class FeignInterceptorConfiguration {

    /**
     * feign远程调用丢失请求头问题
     *
     * @return RequestInterceptor
     */
    @Bean("requestInterceptor")
    public RequestInterceptor requestInterceptor() {
        return requestTemplate -> {
            // 1.使用RequestContextHolder获取在Controller层进入的请求的所有属性,底层是使用ThreadLocal的机制
            HttpServletRequest request = RequestHolder.getHttpServletRequest();
            // 2.同步请求头数据  (Cookie)
            Enumeration<String> headerNames = request.getHeaderNames();
            if (headerNames != null) {
                while (headerNames.hasMoreElements()) {
                    String name = headerNames.nextElement();
                    String values = request.getHeader(name);
                    requestTemplate.header(name, values);
                }
            }
        };
    }

}
