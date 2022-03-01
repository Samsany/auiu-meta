package com.auiucloud.core.security.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 白名单URL配置
 *
 * @author dries
 * @date 2021/12/22
 */
@Data
@RefreshScope
@Component
@ConfigurationProperties(prefix = "auiu-cloud.security.ignore")
public class IgnoreUrlPropsConfig {

    /**
     * 认证中心默认忽略验证地址
     */
    private static final String[] SECURITY_ENDPOINTS = {
            "/error/**",
            "/auth/**",
            "/oauth/token",
            "/login/*",
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

    private List<String> urls = new ArrayList<>();
    private List<String> client = new ArrayList<>();
    private List<String> ignoreSecurity = new ArrayList<>();

    /**
     * 首次加载合并ENDPOINTS
     */
    @PostConstruct
    public void initIgnoreSecurity() {
        Collections.addAll(ignoreSecurity, SECURITY_ENDPOINTS);
    }
}
