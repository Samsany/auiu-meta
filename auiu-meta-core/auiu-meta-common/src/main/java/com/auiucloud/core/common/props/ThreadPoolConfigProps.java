package com.auiucloud.core.common.props;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author dries
 * @date 2021/12/21
 */
@Data
@ConfigurationProperties(prefix = "meta.sync-task.thread")
public class ThreadPoolConfigProps {

    /**
     * 线程池维护线程的最小数量.
     */
    private int corePoolSize = 10;
    /**
     * 线程池维护线程的最大数量
     */
    private int maxPoolSize = 200;
    /**
     * 队列最大长度
     */
    private int queueCapacity = 10;
    /**
     * 线程池空闲线程等待工作的超时时间
     */
    private int keepAliveTime = 10;
    /**
     * 线程池前缀
     */
    private String threadNamePrefix = "MetaExecutor-";

}
