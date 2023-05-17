package com.auiucloud.auth.config;

import com.auiucloud.core.cloud.props.MetaApiProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 预请求配置
 *
 * @author dries
 * @date 2021/12/27
 */
@Configuration
@EnableConfigurationProperties({MetaApiProperties.class})
public class PreRequestConfig {
}
