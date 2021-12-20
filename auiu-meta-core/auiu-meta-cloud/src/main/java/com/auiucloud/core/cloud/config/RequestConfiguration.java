package com.auiucloud.core.cloud.config;

import com.auiucloud.core.cloud.filter.TraceFilter;
import com.auiucloud.core.cloud.props.MetaRequestProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author dries
 * @date 2021/12/20
 */
@Configuration
@EnableConfigurationProperties(MetaRequestProperties.class)
public class RequestConfiguration {

    @Bean
    public TraceFilter traceFilter() {
        return new TraceFilter();
    }

}
