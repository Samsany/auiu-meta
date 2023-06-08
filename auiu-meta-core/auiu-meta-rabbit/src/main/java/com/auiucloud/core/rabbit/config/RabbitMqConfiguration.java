package com.auiucloud.core.rabbit.config;

import com.auiucloud.core.common.utils.YamlPropertyLoaderFactory;
import com.auiucloud.core.rabbit.utils.RabbitMqUtils;
import org.springframework.amqp.core.ReturnedMessage;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

/**
 * RabbitMQ配置
 *
 * @author dries
 * @date 2021/12/22
 */
@AutoConfiguration
@PropertySource(factory = YamlPropertyLoaderFactory.class, value = "classpath:rabbitmq.yml")
public class RabbitMqConfiguration {

    @Resource
    private RabbitTemplate rabbitTemplate;

    @Bean
    public RabbitMqUtils rabbitMqUtils() {
        return new RabbitMqUtils(rabbitTemplate);
    }

    /**
     * 定制 RabbitTemplate
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
             *
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
