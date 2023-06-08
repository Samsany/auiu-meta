package com.auiucloud.core.rabbit.utils;

import com.auiucloud.core.common.api.ResultCode;
import com.auiucloud.core.common.constant.CommonConstant;
import com.auiucloud.core.common.constant.MessageConstant;
import com.auiucloud.core.common.exception.ApiException;
import com.rabbitmq.client.AMQP;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * @author dries
 **/
@Slf4j
@RequiredArgsConstructor
public class RabbitMqUtils {

    private final RabbitTemplate rabbitTemplate;

    public Integer getQueueMessageCount(String queue) {
        AMQP.Queue.DeclareOk declareOk = rabbitTemplate.execute(channel -> channel.queueDeclarePassive(queue));
        if (declareOk != null) {
            log.info("未确认的消息数量：{}", declareOk.getMessageCount());
            return declareOk.getMessageCount();
        }
        throw new ApiException(ResultCode.SERVICE_ERROR_C0120);
    }
}
