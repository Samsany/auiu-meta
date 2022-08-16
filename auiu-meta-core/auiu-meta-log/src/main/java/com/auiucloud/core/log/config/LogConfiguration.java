package com.auiucloud.core.log.config;

import com.auiucloud.core.log.aspect.LogAspect;
import com.auiucloud.core.log.event.LogListener;
import com.auiucloud.core.log.feign.ICommonLogProvider;
import com.auiucloud.core.log.feign.ISysLogProvider;
import com.auiucloud.core.log.props.LogProperties;
import com.auiucloud.core.log.props.LogType;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * 日志配置中心
 *
 * @author dries
 * @date 2021/12/27
 */
@EnableAsync
@AutoConfiguration
@RequiredArgsConstructor
@ConditionalOnWebApplication
@EnableConfigurationProperties(value = LogProperties.class)
public class LogConfiguration {

    private final ISysLogProvider sysLogProvider;
    private final ICommonLogProvider commonLogService;
    private final LogProperties logProperties;

    @Bean
    public LogListener sysLogListener() {
        if (logProperties.getLogType().equals(LogType.KAFKA)) {
            return new LogListener(commonLogService, logProperties);
        }
        return new LogListener(sysLogProvider, logProperties);
    }

    @Bean
    public LogAspect logAspect(ApplicationContext applicationContext) {
        return new LogAspect(applicationContext);
    }

}
