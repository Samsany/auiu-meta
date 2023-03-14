package com.auiucloud.core.oss.config;

import com.auiucloud.core.common.constant.CommonConstant;
import com.auiucloud.core.common.constant.ComponentConstant;
import com.auiucloud.core.oss.core.OssTemplate;
import com.auiucloud.core.oss.props.OssProperties;
import com.auiucloud.core.redis.core.RedisService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * AWS自动配置类
 *
 * @author dries
 **/
@Slf4j
@Configuration
@RequiredArgsConstructor
public class OssConfiguration {

    private final RedisService redisService;

    private final OssProperties properties;

    @Bean
    @ConditionalOnMissingBean(OssTemplate.class)
    @ConditionalOnProperty(name = "oss.enable", havingValue = "true", matchIfMissing = true)
    public OssTemplate ossTemplate() {
        return new OssTemplate(properties);
    }

    @Bean
    @RefreshScope
    public OssProperties ossProperties(){
        return (OssProperties) redisService.get(ComponentConstant.OSS_DEFAULT);
    }
}
