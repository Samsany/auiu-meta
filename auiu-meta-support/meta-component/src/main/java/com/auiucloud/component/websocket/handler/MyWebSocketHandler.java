package com.auiucloud.component.websocket.handler;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONUtil;
import com.auiucloud.component.sd.service.IAiDrawService;
import com.auiucloud.component.websocket.utils.WebSocketUtil;
import com.auiucloud.core.common.api.ApiResult;
import com.auiucloud.core.common.api.ResultCode;
import com.auiucloud.core.common.constant.CommonConstant;
import com.auiucloud.core.common.constant.MessageConstant;
import com.auiucloud.core.common.constant.Oauth2Constant;
import com.auiucloud.core.common.enums.IBaseEnum;
import com.auiucloud.core.common.enums.WsMessageEnums;
import com.auiucloud.core.common.exception.ApiException;
import com.auiucloud.core.common.model.WsMsgModel;
import com.auiucloud.core.common.utils.StringPool;
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
public class MyWebSocketHandler extends AbstractWebSocketHandler {

    private final IAiDrawService aiDrawService;
    private final StreamBridge streamBridge;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        Map<String, Object> attributes = session.getAttributes();
        String loginType = (String) attributes.get(Oauth2Constant.META_LOGIN_TYPE);
        String token = (String) attributes.get(Oauth2Constant.META_USER_TOKEN);
        Long userId = (Long) attributes.get(Oauth2Constant.META_USER_ID);

        log.info("MyWebSocketHandler, 收到新的连接: {}, 用户：{}", session.getId(), userId);
        WebSocketUtil.putUser(loginType + StringPool.AT + token, String.valueOf(userId), session);
    }

    /**
     * 文本消息
     *
     * @param session 消息通道
     * @param message 消息
     * @throws Exception 异常信息
     */
    @Override
    protected void handleTextMessage(@NotNull WebSocketSession session, @NotNull TextMessage message) throws Exception {
        // log.info("MyWebSocketHandler, 连接：{}, 已收到消息。", session.getId());
        Map<String, Object> attributes = session.getAttributes();
        Long userId = (Long) attributes.get("userId");
        // String token = (String) attributes.get("token");
        try {
            WsMsgModel<Object> payload = JSONUtil.toBean(message.getPayload(), WsMsgModel.class, true);
            payload.setFrom(String.valueOf(userId));
            String code = payload.getCode();
            // 获取消息类型
            WsMessageEnums.TypeEnum messageType = IBaseEnum.getEnumByValue(code, WsMessageEnums.TypeEnum.class);
            if (ObjectUtil.isNotNull(messageType)) {
                // 消息队列发送消息
                switch (messageType) {
                    case SD_TXT2IMG -> {
                        try {
                            aiDrawService.sdTxt2ImgHandleMessage(session, payload);
                        } catch (Exception e) {
                            ApiResult<Object> apiResult = ApiResult.fail(e.getMessage());
                            apiResult.setData(payload.getContent());

                            if (e.getMessage().equals(ResultCode.USER_ERROR_A0160.getMessage())) {
                                apiResult = WebSocketUtil.buildSendErrorMessageModel(ResultCode.USER_ERROR_A0160);
                            }
                            streamBridge.send(MessageConstant.NOTICE_MESSAGE_OUTPUT, WsMsgModel.builder()
                                    .code(WsMessageEnums.TypeEnum.SD_TXT2IMG.getValue())
                                    .sendType(WsMessageEnums.SendTypeEnum.USER.getValue())
                                    .from(String.valueOf(CommonConstant.SYSTEM_NODE_ID))
                                    .to(String.valueOf(userId))
                                    .content(apiResult)
                                    .build());
                        }
                    }
                    case SD_IMG2IMG -> {
                        try {
                            aiDrawService.sdImg2ImgHandleMessage(session, payload);
                        } catch (Exception e) {
                            ApiResult<Object> apiResult = ApiResult.fail(e.getMessage());
                            apiResult.setData(payload.getContent());

                            if (e.getMessage().equals(ResultCode.USER_ERROR_A0160.getMessage())) {
                                apiResult = WebSocketUtil.buildSendErrorMessageModel(ResultCode.USER_ERROR_A0160);
                            }
                            streamBridge.send(MessageConstant.NOTICE_MESSAGE_OUTPUT, WsMsgModel.builder()
                                    .code(WsMessageEnums.TypeEnum.SD_IMG2IMG.getValue())
                                    .sendType(WsMessageEnums.SendTypeEnum.USER.getValue())
                                    .from(String.valueOf(CommonConstant.SYSTEM_NODE_ID))
                                    .to(String.valueOf(userId))
                                    .content(apiResult)
                                    .build());
                        }
                    }
                }
            } else {
                throw new ApiException(ResultCode.SERVICE_ERROR_C0125);
            }
        } catch (Exception e) {
            log.error("处理WS消息异常: {}", e.getMessage());
            streamBridge.send(MessageConstant.NOTICE_MESSAGE_OUTPUT, WsMsgModel.builder()
                    .code(WsMessageEnums.TypeEnum.ERROR.getValue())
                    .sendType(WsMessageEnums.SendTypeEnum.USER.getValue())
                    .from(String.valueOf(CommonConstant.SYSTEM_NODE_ID))
                    .to(String.valueOf(userId))
                    .content(ApiResult.fail(e.getMessage()))
                    .build());
            // log.error("[SD文生图异常，失败信息:{}]", e.getMessage());
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
    protected void handleBinaryMessage(WebSocketSession session, @NotNull BinaryMessage message) throws Exception {
        log.info("MyWebSocketHandler, 连接：{}, 已收到二进制消息。", session.getId());
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
    protected void handlePongMessage(WebSocketSession session, @NotNull PongMessage message) throws Exception {
        log.info("MyWebSocketHandler, 连接：{}, 已收到pong消息。", session.getId());
        super.handlePongMessage(session, message);
    }

    @Override
    public void handleTransportError(@NotNull WebSocketSession session, @NotNull Throwable exception) throws Exception {
        log.error("WS 连接发生错误");
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, @NotNull CloseStatus closeStatus) throws Exception {
        log.info("WS 关闭连接");
        Map<String, Object> attributes = session.getAttributes();
        String loginType = (String) attributes.get(Oauth2Constant.META_LOGIN_TYPE);
        String token = (String) attributes.get(Oauth2Constant.META_USER_TOKEN);
        Long userId = (Long) attributes.get(Oauth2Constant.META_USER_ID);
        WebSocketUtil.removeUser(loginType + StringPool.AT + token, String.valueOf(userId));
    }

    // 支持分片消息
    @Override
    public boolean supportsPartialMessages() {
        return true;
    }
}
