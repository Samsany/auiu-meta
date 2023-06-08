package com.auiucloud.component.websocket.handle;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONUtil;
import com.auiucloud.component.cms.enums.SdDrawEnums;
import com.auiucloud.component.cms.vo.SdWaitQueueVO;
import com.auiucloud.component.websocket.utils.WebSocketUtil;
import com.auiucloud.core.common.api.ResultCode;
import com.auiucloud.core.common.constant.CommonConstant;
import com.auiucloud.core.common.constant.MessageConstant;
import com.auiucloud.core.common.enums.IBaseEnum;
import com.auiucloud.core.common.enums.MessageEnums;
import com.auiucloud.core.common.model.WsMsgModel;
import com.auiucloud.core.rabbit.utils.RabbitMqUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.AbstractWebSocketHandler;

import java.util.Map;

/**
 * @author dries
 **/
@Slf4j
@RequiredArgsConstructor
public class SpringSocketHandle extends AbstractWebSocketHandler {

    private final StreamBridge streamBridge;
    private final RabbitMqUtils rabbitMqUtils;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        Map<String, Object> attributes = session.getAttributes();
        String token = (String) attributes.get("token");
        Long userId = (Long) attributes.get("userId");

        log.info("SpringSocketHandle, 收到新的连接: {}, 用户：{}", session.getId(), userId);
        WebSocketUtil.putUser(token, String.valueOf(userId), session);
    }

    /**
     * 文本消息
     *
     * @param session
     * @param message
     * @throws Exception
     */
    @Override
    protected void handleTextMessage(@NotNull WebSocketSession session, TextMessage message) throws Exception {
        // log.info("SpringSocketHandle, 连接：{}, 已收到消息。", session.getId());
        String msgStr = ((CharSequence) message.getPayload()).toString();
        try {
            WsMsgModel wsMsgModel = JSONUtil.toBean(msgStr, WsMsgModel.class);
            Map<String, Object> attributes = session.getAttributes();
            String token = (String) attributes.get("token");
            Long userId = (Long) attributes.get("userId");
            // 覆盖传递的发送用户ID
            wsMsgModel.setFrom(String.valueOf(userId));

            // log.info("消息内容: {}", wsMsgModel.getContent());
            String code = wsMsgModel.getCode();
            // 获取消息类型
            MessageEnums.WsMessageTypeEnum messageType = IBaseEnum.getEnumByValue(code, MessageEnums.WsMessageTypeEnum.class);
            if (ObjectUtil.isNotNull(messageType)) {
                // 消息队列发送消息
                switch (messageType) {
                    case SD_TXT2IMG -> {
                        Integer queueMessageCount = rabbitMqUtils.getQueueMessageCount(MessageConstant.SD_TXT2IMG_MESSAGE_QUEUE);
                        // System.out.println("当前排队数量"+queueMessageCount);
                        session.sendMessage(new TextMessage(WebSocketUtil.buildSendMessageModel(
                                WsMsgModel.builder()
                                        .code(MessageEnums.WsMessageTypeEnum.SD_TXT2IMG_QUEUE.getValue())
                                        .from(String.valueOf(CommonConstant.SYSTEM_NODE_ID))
                                        .to(String.valueOf(userId))
                                        .content(SdWaitQueueVO.builder()
                                                .queueMessageCount(queueMessageCount)
                                                .changeType(SdDrawEnums.QueueChangeType.INCREASE.getValue())
                                                .build())
                                        .build()
                        )
                        ));
                        streamBridge.send(MessageConstant.SD_TXT2IMG_MESSAGE_OUTPUT, wsMsgModel);
                    }
                }
            } else {
                session.sendMessage(new TextMessage(
                        WebSocketUtil.buildSendMessageModel(WsMsgModel.builder()
                                .code(MessageEnums.WsMessageTypeEnum.ERROR.getValue())
                                .content(WebSocketUtil.buildSendErrorMessageModel(ResultCode.SERVICE_ERROR_C0125))
                                .build())));
            }
        } catch (Exception e) {
            session.sendMessage(new TextMessage(
                    WebSocketUtil.buildSendMessageModel(WsMsgModel.builder()
                            .code(MessageEnums.WsMessageTypeEnum.ERROR.getValue())
                            .content(WebSocketUtil.buildSendErrorMessageModel(ResultCode.SERVICE_ERROR_C0121))
                            .build())));
        }

    }

    /**
     * 二进制消息体
     *
     * @param session
     * @param message
     * @throws Exception
     */
    @Override
    protected void handleBinaryMessage(WebSocketSession session, BinaryMessage message) throws Exception {
        log.info("SpringSocketHandle, 连接：{}, 已收到二进制消息。", session.getId());
        super.handleBinaryMessage(session, message);
    }

    /**
     * pong是响应消息 它也可以用作单向心跳消息
     *
     * @param session
     * @param message
     * @throws Exception
     */
    @Override
    protected void handlePongMessage(WebSocketSession session, PongMessage message) throws Exception {
        log.info("SpringSocketHandle, 连接：{}, 已收到pong消息。", session.getId());
        super.handlePongMessage(session, message);
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        log.error("WS 连接发生错误");
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
        log.info("WS 关闭连接");
        Map<String, Object> attributes = session.getAttributes();
        String token = (String) attributes.get("token");
        Long userId = (Long) attributes.get("userId");
        WebSocketUtil.removeUser(token, String.valueOf(userId));
    }

    // 支持分片消息
    @Override
    public boolean supportsPartialMessages() {
        return true;
    }
}
