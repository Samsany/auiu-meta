package com.auiucloud.component.message.consumer;

import com.auiucloud.component.websocket.utils.WebSocketUtil;
import com.auiucloud.core.common.enums.IBaseEnum;
import com.auiucloud.core.common.enums.WsMessageEnums;
import com.auiucloud.core.common.model.WsMsgModel;
import com.rabbitmq.client.Channel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.context.annotation.Bean;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.function.Consumer;

/**
 * @author dries
 **/
@Slf4j
@Service
@RequiredArgsConstructor
public class NoticeConsumerService {

    /**
     * 函数式编辑接收消息
     *
     * @return Consumer<String>
     */
    @Bean
    public Consumer<Message<WsMsgModel>> notice() {
        return message -> {
            WsMsgModel payload = message.getPayload();
            log.debug("[开始消费消息队列通知数据...]");
            Channel channel = message.getHeaders().get(AmqpHeaders.CHANNEL, Channel.class);
            Long deliveryTag = message.getHeaders().get(AmqpHeaders.DELIVERY_TAG, Long.class);
            if (channel != null && deliveryTag != null) {
                try {
                    log.debug("监听者收到消息：{}", message.getPayload());
                    WsMessageEnums.SendTypeEnum enumByValue = IBaseEnum.getEnumByValue(payload.getSendType(), WsMessageEnums.SendTypeEnum.class);
                    switch (enumByValue) {
                        case USER -> WebSocketUtil.sendMessageToUser(payload);
                        case IN_INCLUDE_USER -> WebSocketUtil.sendMessageToInUser(payload);
                        case EX_INCLUDE_USER -> WebSocketUtil.sendMessageToExUser(payload);
                        case ALL -> WebSocketUtil.sendMessageToAll(payload);
                    }
                    // 签收消息
                    channel.basicAck(deliveryTag, false);
                } catch (Exception e) {
                    log.error("[SD消息通知发送处理异常，异常信息:{}]", e.getMessage());
                    try {
                        // channel.basicReject(deliveryTag, true);
                        channel.basicAck(deliveryTag, false);
                    } catch (IOException ex) {
                        log.error("[SD消息通知退回处理异常，异常信息:{} ]", ex.getMessage());
                        // todo 持久化处理消息
                        throw new RuntimeException(ex);
                    }
                }
            }
        };
    }

}
