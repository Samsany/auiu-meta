package com.auiucloud.core.rabbit.config;

import org.springframework.amqp.core.ReturnedMessage;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

/**
 * RabbitMQ配置
 *
 * @author dries
 * @date 2021/12/22
 */
@Configuration
public class RabbitMqConfiguration {

    @Resource
    RabbitTemplate rabbitTemplate;

    /**
     * 使用JSON序列化机制，进行消息转换
     */
    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    /**
     * 定制RabbitTemplate
     * 注解@PostConstruct: MyRabbitMqConfig对象创建完成后，执行这个方法
     */
    @PostConstruct
    public void initRabbitTemplate() {
        // 设置确认回调
        rabbitTemplate.setConfirmCallback(new RabbitTemplate.ConfirmCallback() {
            /**
             * 1. 只要消息到达服务器 Broker就 ack = true
             * @param correlationData 当前消息的唯一关联数据（消息的唯一ID）
             * @param ack 消息是否成功收到
             * @param cause 消息失败的原因
             */
            @Override
            public void confirm(CorrelationData correlationData, boolean ack, String cause) {
                // TODO 消息入库
                System.out.println("confirm...correlationData【" + correlationData + "】===> ack【" + ack + "】===> cause【" + cause + "】");
            }
        });

        // 设置消息抵达队列的确认回调
        rabbitTemplate.setReturnsCallback(new RabbitTemplate.ReturnsCallback() {
            /**
             * 只要消息没有投递给指定的队列，就触发这个失败回调
             * @param returned 投递失败的信息
             */
            @Override
            public void returnedMessage(ReturnedMessage returned) {
                // TODO 消息入库，重试
                System.out.println("Fail Message[" + returned.getMessage() + "]");
            }
        });
    }
}
