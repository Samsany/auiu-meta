// package com.auiucloud.component.message.consumer;
//
// import lombok.extern.slf4j.Slf4j;
// import org.springframework.context.annotation.Bean;
// import org.springframework.stereotype.Service;
//
// import java.util.function.Consumer;
//
///**
// * @author dries
// **/
//@Slf4j
//@Service
// public class EmailConsumerService {
//
//    /**
//     * 消费分布式事务消息
//     */
//    @Bean
//    public Consumer<String> email() {
//        return email -> {
//            log.info("接收的普通消息为：{}", email);
//        };
//    }
//
//}
