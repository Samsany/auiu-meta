package com.auiucloud.core.log.config;

import org.slf4j.TransmitLocalMDCAdapter;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * 初始化TransmitLocalMDCAdapter，并替换MDC中的adapter对象
 *
 * @author dries
 * @date 2021/12/28
 */
public class TransmitLocalMDCInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
    @Override
    @SuppressWarnings("all")
    public void initialize(ConfigurableApplicationContext applicationContext) {
        // 加载TtlMDCAdapter实例
        TransmitLocalMDCAdapter.getInstance();
    }
}
