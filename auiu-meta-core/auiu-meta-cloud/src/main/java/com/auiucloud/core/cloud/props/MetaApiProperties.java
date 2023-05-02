package com.auiucloud.core.cloud.props;

import cn.hutool.extra.spring.SpringUtil;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * @author dries
 * @date 2021/12/20
 */
@Data
@Component
@RefreshScope
@ConfigurationProperties(prefix = "meta.auth")
public class MetaApiProperties implements InitializingBean {

    private static final Pattern PATTERN = Pattern.compile("\\{(.*?)\\}");

    /**
     * 监控中心和swagger需要访问的url
     */
    private static final String[] DEFAULT_IGNORE_URLS = {
            "/error/**",
            "/actuator/**",
            "/v3/api-docs/**",
    };

    /**
     * 单机登录，默认false
     */
    private Boolean singleLogin = false;
    /**
     * 忽略URL，List列表形式
     */
    @Getter
    @Setter
    private List<String> ignoreUrls = new ArrayList<>();

    /**
     * 是否启用网关鉴权模式
     */
    private Boolean enabled = true;

    /**
     * 加载合并DEFAULT_IGNORE_URLS
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        ignoreUrls.addAll(Arrays.asList(DEFAULT_IGNORE_URLS));
        RequestMappingHandlerMapping mapping = SpringUtil.getBean("requestMappingHandlerMapping");
        Map<RequestMappingInfo, HandlerMethod> map = mapping.getHandlerMethods();

        map.keySet().forEach(info -> {
            HandlerMethod handlerMethod = map.get(info);

            // 获取方法上边的注解 替代path variable 为 *
//            Inner method = AnnotationUtils.findAnnotation(handlerMethod.getMethod(), Inner.class);
//            Optional.ofNullable(method).ifPresent(inner -> Objects.requireNonNull(info.getPathPatternsCondition())
//                    .getPatternValues().forEach(url -> urls.add(ReUtil.replaceAll(url, PATTERN, "*"))));
//
//            // 获取类上边的注解, 替代path variable 为 *
//            Inner controller = AnnotationUtils.findAnnotation(handlerMethod.getBeanType(), Inner.class);
//            Optional.ofNullable(controller).ifPresent(inner -> Objects.requireNonNull(info.getPathPatternsCondition())
//                    .getPatternValues().forEach(url -> ignoreUrls.add(ReUtil.replaceAll(url, PATTERN, "*"))));
        });
    }
}
