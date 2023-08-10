package com.auiucloud.component.message.consumer;

import cn.hutool.json.JSONUtil;
import com.auiucloud.component.sd.dto.SdImg2ImgConfigDTO;
import com.auiucloud.component.sd.dto.SdTxt2ImgConfigDTO;
import com.auiucloud.component.sd.service.IAiDrawService;
import com.auiucloud.component.websocket.utils.WebSocketUtil;
import com.auiucloud.core.common.api.ApiResult;
import com.auiucloud.core.common.api.ResultCode;
import com.auiucloud.core.common.constant.CommonConstant;
import com.auiucloud.core.common.constant.MessageConstant;
import com.auiucloud.core.common.enums.IBaseEnum;
import com.auiucloud.core.common.enums.WsMessageEnums;
import com.auiucloud.core.common.model.WsMsgModel;
import com.rabbitmq.client.Channel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.cloud.stream.function.StreamBridge;
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
public class SdGenImgConsumerService {

    private final IAiDrawService aiDrawService;
    private final StreamBridge streamBridge;

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
            String code = payload.getCode();

            log.debug("[开始消费文生图消息队列数据...]");
            Channel channel = message.getHeaders().get(AmqpHeaders.CHANNEL, Channel.class);
            Long deliveryTag = message.getHeaders().get(AmqpHeaders.DELIVERY_TAG, Long.class);
            if (channel != null && deliveryTag != null) {
                try {
                    log.debug("监听者收到消息：{}", message.getPayload());

                    WsMessageEnums.TypeEnum typeEnum = IBaseEnum.getEnumByValue(code, WsMessageEnums.TypeEnum.class);

                    switch (typeEnum) {
                        case SD_TXT2IMG -> {
                            // 获取参数
                            SdTxt2ImgConfigDTO sdDrawParam = JSONUtil.toBean(JSONUtil.toJsonStr(payload.getContent()), SdTxt2ImgConfigDTO.class);

                            // 开始生成图片
                            aiDrawService.sdText2Img(sdDrawParam);
                        }

                        case SD_IMG2IMG -> {
                            // 获取参数
                            SdImg2ImgConfigDTO sdDrawParam = JSONUtil.toBean(JSONUtil.toJsonStr(payload.getContent()), SdImg2ImgConfigDTO.class);
                            // 开始生成图片
                            aiDrawService.sdImg2Img(sdDrawParam);
                        }
                    }

                    // channel.basicReject(deliveryTag, true);
                } catch (Exception e) {
                    log.error("[SD文生图异常，失败信息:{}]", e.getMessage());
                    ApiResult<Object> apiResult = WebSocketUtil.buildSendErrorMessageModel(ResultCode.ERROR);
                    apiResult.setMessage(e.getMessage());
                    apiResult.setData(payload.getContent());
                    streamBridge.send(MessageConstant.NOTICE_MESSAGE_OUTPUT, WsMsgModel.builder()
                            .code(code)
                            .sendType(WsMessageEnums.SendTypeEnum.USER.getValue())
                            .from(String.valueOf(CommonConstant.SYSTEM_NODE_ID))
                            .to(payload.getFrom())
                            .content(apiResult)
                            .build());
                } finally {
                    try {
                        // channel.basicNack(deliveryTag, false, false); // 批量处理拒绝
                        // channel.basicReject(deliveryTag, true); // 单个处理
                        channel.basicAck(deliveryTag, false);
                    } catch (IOException e) {
                        log.error("[SD文生图消息签收处理异常，异常信息:{}]", e.getMessage());
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
