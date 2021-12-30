package com.auiucloud.core.log.config;

import com.auiucloud.core.log.event.LogListener;
import com.auiucloud.core.log.feign.ICommonLogService;
import com.auiucloud.core.log.feign.ISysLogService;
import com.auiucloud.core.log.props.LogProperties;
import com.auiucloud.core.log.props.LogType;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * 日志配置中心
 *
 * @author dries
 * @date 2021/12/27
 */
@EnableAsync
@Configuration
@RequiredArgsConstructor
@ConditionalOnWebApplication
@EnableConfigurationProperties(value = LogProperties.class)
public class LogConfiguration {

    private final ISysLogService logService;
    private final ICommonLogService commonLogService;
    private final LogProperties logProperties;

    @Bean
    public LogListener sysLogListener() {
        if (logProperties.getLogType().equals(LogType.KAFKA)) {
            return new LogListener(commonLogService, logProperties);
        }
        return new LogListener(logService, logProperties);
    }
}
