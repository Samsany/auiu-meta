package com.auiucloud.core.web.config;

import com.auiucloud.core.common.utils.YamlPropertyLoaderFactory;
import com.auiucloud.core.web.handler.GlobalExceptionHandler;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;

/**
 * @author dries
 * @date 2021/12/22
 */
@AutoConfiguration
@ComponentScan(value = "com.auiucloud.core.web.handler")
@PropertySource(factory = YamlPropertyLoaderFactory.class, value = "classpath:error.yml")
public class ExceptionConfiguration {

    @Bean
    public GlobalExceptionHandler baseExceptionHandler() {
        return new GlobalExceptionHandler();
    }

}
