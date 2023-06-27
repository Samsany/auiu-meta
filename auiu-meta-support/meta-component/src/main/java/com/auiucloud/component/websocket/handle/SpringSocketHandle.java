package com.auiucloud.component.websocket.handle;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONUtil;
import com.auiucloud.component.sd.service.IAiDrawService;
import com.auiucloud.component.websocket.utils.WebSocketUtil;
import com.auiucloud.core.common.api.ResultCode;
import com.auiucloud.core.common.enums.IBaseEnum;
import com.auiucloud.core.common.enums.MessageEnums;
import com.auiucloud.core.common.model.WsMsgModel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.AbstractWebSocketHandler;

import java.util.Map;

/**
 * @author dries
 **/
@Slf4j
@RequiredArgsConstructor
public class SpringSocketHandle extends AbstractWebSocketHandler {

    private final IAiDrawService aiDrawService;

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
     * @param session 消息通道
     * @param message 消息
     * @throws Exception 异常信息
     */
    @Override
    protected void handleTextMessage(@NotNull WebSocketSession session, TextMessage message) throws Exception {
        // log.info("SpringSocketHandle, 连接：{}, 已收到消息。", session.getId());
        String msgStr = ((CharSequence) message.getPayload()).toString();
        try {
            WsMsgModel payload = JSONUtil.toBean(msgStr, WsMsgModel.class);
            Map<String, Object> attributes = session.getAttributes();
            // String token = (String) attributes.get("token");
            Long userId = (Long) attributes.get("userId");
            // 覆盖传递的发送用户ID
            payload.setFrom(String.valueOf(userId));
            String code = payload.getCode();
            // 获取消息类型
            MessageEnums.WsMessageTypeEnum messageType = IBaseEnum.getEnumByValue(code, MessageEnums.WsMessageTypeEnum.class);
            if (ObjectUtil.isNotNull(messageType)) {
                // 消息队列发送消息
                switch (messageType) {
                    case SD_TXT2IMG -> {
                        aiDrawService.sdTxt2ImgHandleMessage(session, payload);
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
                            .content(WebSocketUtil.buildSendErrorMessageModel(ResultCode.ERROR))
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
