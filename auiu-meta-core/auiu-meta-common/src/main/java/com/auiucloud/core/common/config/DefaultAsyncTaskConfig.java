package com.auiucloud.core.common.config;

import com.auiucloud.core.common.props.ThreadPoolConfigProps;
import com.auiucloud.core.common.utils.CustomThreadPoolTaskExecutor;
import lombok.Data;
import org.springframework.context.annotation.Bean;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import javax.annotation.Resource;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 异步任务配置
 *
 * @author dries
 * @date 2021/12/21
 */
@Data
@EnableAsync(proxyTargetClass = true)
public class DefaultAsyncTaskConfig {

    @Resource
    private ThreadPoolConfigProps threadPoolConfigProps;

    @Bean
    public TaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor executor = new CustomThreadPoolTaskExecutor();
        executor.setCorePoolSize(threadPoolConfigProps.getCorePoolSize());
        executor.setMaxPoolSize(threadPoolConfigProps.getMaxPoolSize());
        executor.setQueueCapacity(threadPoolConfigProps.getQueueCapacity());
        executor.setKeepAliveSeconds(threadPoolConfigProps.getKeepAliveTime());
        executor.setThreadNamePrefix(threadPoolConfigProps.getThreadNamePrefix());
        /*
           rejection-policy：当pool已经达到max size的时候，如何处理新任务
           CALLER_RUNS：不在新线程中执行任务，而是有调用者所在的线程来执行
        */
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.initialize();
        return executor;
    }

}
