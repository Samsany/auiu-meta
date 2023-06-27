package com.auiucloud.component.message.consumer;

import cn.hutool.json.JSONUtil;
import com.auiucloud.component.sd.enums.SdDrawEnums;
import com.auiucloud.component.sd.service.IAiDrawService;
import com.auiucloud.component.sd.dto.SdTxt2ImgConfigDTO;
import com.auiucloud.component.sd.vo.SdWaitQueueVO;
import com.auiucloud.component.websocket.utils.WebSocketUtil;
import com.auiucloud.core.common.api.ApiResult;
import com.auiucloud.core.common.api.ResultCode;
import com.auiucloud.core.common.constant.CommonConstant;
import com.auiucloud.core.common.enums.MessageEnums;
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
public class SdText2ImgConsumerService {

    private final IAiDrawService aiDrawService;

    /**
     * 函数式编辑接收消息
     *
     * @return Consumer<String>
     */
    @Bean
    public Consumer<Message<WsMsgModel>> txt2img() {
        return message -> {
            // 开始生成图片
            WsMsgModel payload = message.getPayload();

            log.debug("[开始消费文生图消息队列数据...]");
            Channel channel = message.getHeaders().get(AmqpHeaders.CHANNEL, Channel.class);
            Long deliveryTag = message.getHeaders().get(AmqpHeaders.DELIVERY_TAG, Long.class);
            if (channel != null && deliveryTag != null) {
                try {
                    log.debug("监听者收到消息：{}", message.getPayload());
                    // 通知其它用户当前队列数量
                    WebSocketUtil.sendMessageToOther(WsMsgModel.builder()
                            .code(MessageEnums.WsMessageTypeEnum.SD_TXT2IMG_QUEUE.getValue())
                            .from(payload.getFrom())
                            .to(String.valueOf(CommonConstant.SYSTEM_NODE_ID))
                            .content(SdWaitQueueVO.builder()
                                    .queueMessageCount(CommonConstant.STATUS_DISABLE_VALUE)
                                    .changeType(SdDrawEnums.QueueChangeType.DECREASE.getValue())
                                    .build())
                            .build());

                    // 开始生成图片
                    SdTxt2ImgConfigDTO sdDrawParam = JSONUtil.toBean(JSONUtil.toJsonStr(payload.getContent()), SdTxt2ImgConfigDTO.class);
                    aiDrawService.sdText2Img(sdDrawParam);
                    // channel.basicReject(deliveryTag, true);
                } catch (Exception e) {
                    log.error("[SD文生图异常，失败信息:{}]", e.getMessage());
                    WebSocketUtil.sendMessage(WsMsgModel.builder()
                            .code(MessageEnums.WsMessageTypeEnum.ERROR.getValue())
                            .from(String.valueOf(CommonConstant.SYSTEM_NODE_ID))
                            .to(payload.getFrom())
                            .content(WebSocketUtil.buildSendErrorMessageModel(ResultCode.SERVICE_ERROR_C0122))
                            .build());
                } finally {
                    try {
                        // channel.basicNack(deliveryTag, false, false); // 批量处理拒绝
                        // channel.basicReject(deliveryTag, true); // 单个处理
                        channel.basicAck(deliveryTag, false);
                    } catch (IOException e) {
                        log.error("[SD文生图消息签收处理异常，异常信息:{}]", e.getMessage());
                        // WebSocketUtil.sendMessage(WsMsgModel.builder()
                        //         .code(MessageEnums.WsMessageTypeEnum.ERROR.getValue())
                        //         .from(String.valueOf(CommonConstant.SYSTEM_NODE_ID))
                        //         .to(payload.getFrom())
                        //         .content(WebSocketUtil.buildSendErrorMessageModel(ResultCode.SERVICE_ERROR_C0122))
                        //         .build());
                    }
                }
            }
        };
    }

    // @RabbitListener(queues = {MessageConstant.SD_TXT2IMG_MESSAGE_DLQ_QUEUE})
    // public void deadLetterListener(org.springframework.amqp.core.Message message) {
    //     Map<String, Object> headers = message.getMessageProperties().getHeaders();
    //
    //     log.info("Received Dead Letter message: {}", message);
    // }
}
