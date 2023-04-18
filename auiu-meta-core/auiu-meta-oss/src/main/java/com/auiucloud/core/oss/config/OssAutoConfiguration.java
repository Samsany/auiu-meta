package com.auiucloud.core.oss.config;

import com.auiucloud.core.common.constant.RedisKeyConstant;
import com.auiucloud.core.oss.controller.OssEndpoint;
import com.auiucloud.core.oss.core.OssTemplate;
import com.auiucloud.core.oss.props.OssProperties;
import com.auiucloud.core.redis.core.RedisService;
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
public class OssAutoConfiguration {

    /**
     * OSS操作模板
     * @return OSS操作模板
     */
    @Bean
    @ConditionalOnMissingBean(OssTemplate.class)
    @ConditionalOnProperty(name = "meta.oss.enable", havingValue = "true", matchIfMissing = true)
    public OssTemplate ossTemplate(OssProperties ossProperties) {
        return new OssTemplate(ossProperties);
    }

    /**
     * OSS端点信息
     * @param ossTemplate oss操作模版
     * @return oss远程服务端点
     */
    @Bean
    @ConditionalOnProperty(name = "meta.oss.info", havingValue = "true")
    public OssEndpoint ossEndpoint(OssTemplate ossTemplate) {
        return new OssEndpoint(ossTemplate);
    }

    @Bean
    @RefreshScope
    public OssProperties ossProperties(RedisService redisService){
        return (OssProperties) redisService.get(RedisKeyConstant.OSS_DEFAULT_CONFIG);
    }

}
