package com.auiucloud.core.security.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author dries
 * @date 2022/3/1
 */
@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "meta.security")
public class AuthPropsConfig {

    /**
     * 认证中心默认忽略验证地址
     */
    private static final String[] SECURITY_ENDPOINTS = {
            "/error/**",
            "/actuator/**",
            "/druid/**",
            "/v2/api-docs/**",
            "/v3/api-docs/**",
            "/doc.html",
            "/webjars/**",
            "/assets/**",
            "**/favicon.ico",
            "/swagger-resources/**"
    };
    /**
     * 单机登录，默认false
     */
    private boolean singleLogin;
    /**
     * 认证中心忽略地址配置
     */
    private List<String> ignoreUrls = new ArrayList<>();

    /**
     * 首次加载合并ENDPOINTS
     */
    @PostConstruct
    public void initIgnoreSecurity() {
        Collections.addAll(ignoreUrls, SECURITY_ENDPOINTS);
    }

}
