package com.auiucloud.component.message.consumer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.util.function.Consumer;

/**
 * @author dries
 **/
@Slf4j
@Service
public class SmsConsumerService {

    /**
     * 函数式编辑接收消息
     *
     * @return
     */
    @Bean
    public Consumer<String> sms() {
        return message -> {
            log.info("接收的普通消息为：{}", message);
        };
    }

}
