package com.auiucloud.core.web.config;

import com.auiucloud.core.common.utils.YamlPropertyLoaderFactory;
import com.auiucloud.core.web.handler.BaseExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * @author dries
 * @date 2021/12/22
 */
@Configuration
@ComponentScan(value = "com.auiucloud.core.web.handler")
@PropertySource(factory = YamlPropertyLoaderFactory.class, value = "classpath:error.yml")
public class ExceptionConfiguration {

    @Bean
    public BaseExceptionHandler baseExceptionHandler() {
        return new BaseExceptionHandler();
    }

}
