package com.auiucloud.core.cloud.props;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author dries
 * @date 2021/12/20
 */
@Data
@Component
@RefreshScope
@ConfigurationProperties(prefix = "meta.auth")
public class MetaApiProperties {

    /**
     * 监控中心和swagger需要访问的url
     */
    private static final String[] ENDPOINTS = {
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
            "/favicon.ico",
            "/swagger-resources/**",
            "/*/rsa/publicKey"
    };

    /**
     * 忽略URL，List列表形式
     */
    private List<String> ignoreUrl = new ArrayList<>();

    /**
     * 是否启用网关鉴权模式
     */
    private boolean enabled = true;

    /**
     * 首次加载合并ENDPOINTS
     */
    @PostConstruct
    public void initIgnoreUrl() {
        Collections.addAll(ignoreUrl, ENDPOINTS);
    }

}
