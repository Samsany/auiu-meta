package com.auiucloud.component.websocket.utils;

import cn.hutool.json.JSONUtil;
import com.auiucloud.core.common.api.ApiResult;
import com.auiucloud.core.common.api.ResultCode;
import com.auiucloud.core.common.constant.CommonConstant;
import com.auiucloud.core.common.model.WsMsgModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author dries
 **/
@Slf4j
public class WebSocketUtil {

    public static final ConcurrentHashMap<String, ConcurrentHashMap<String, WebSocketSession>> users = new ConcurrentHashMap<>();

    public static void putUser(String token, String userId, WebSocketSession session) {
        ConcurrentHashMap<String, WebSocketSession> userMap = WebSocketUtil.users.get(userId);
        if (userMap == null) {
            userMap = new ConcurrentHashMap<>();
            userMap.put(token, session);
            users.put(userId, userMap);
        } else {
            users.get(userId).put(token, session);
        }
    }

    public static void removeUser(String token, String userId) {
        ConcurrentHashMap<String, WebSocketSession> userMap = WebSocketUtil.users.get(userId);
        userMap.remove(token);
    }

    public static void sendMessage(WsMsgModel msgModel) {
        ConcurrentHashMap<String, WebSocketSession> user = users.get(msgModel.getTo());
        if (user != null) {
            batchSendWebsocketMessage(msgModel, user);
        }
    }

    public static void sendMessageToOther(WsMsgModel msgModel) {
        users.forEach((userId, userMap) -> {
            if (!userMap.containsKey(msgModel.getFrom())) {
                msgModel.setFrom(String.valueOf(CommonConstant.SYSTEM_NODE_ID));
                batchSendWebsocketMessage(msgModel, userMap);
            }
        });
        // users.forEachValue(100L, user -> {
        //     if (!user.containsKey(msgModel.getFrom())) {
        //         msgModel.setFrom(String.valueOf(CommonConstant.SYSTEM_NODE_ID));
        //         batchSendWebsocketMessage(msgModel, user);
        //     }
        // });
    }

    public static void sendMessageToAll(WsMsgModel msgModel) {
        msgModel.setFrom(String.valueOf(CommonConstant.SYSTEM_NODE_ID));
        users.values()
                .parallelStream()
                .forEach((user) -> {
                    if (user != null) {
                        batchSendWebsocketMessage(msgModel, user);
                    }
                });
    }

    private static void batchSendWebsocketMessage(WsMsgModel msgModel, ConcurrentHashMap<String, WebSocketSession> user) {
        user.values()
                .forEach(session -> {
                    try {
                        String jsonStr = JSONUtil.toJsonStr(msgModel);
                        // log.info("SpringSocketHandle消息推送，客户端：{}, 推送内容：{}", session.getId(), jsonStr);
                        session.sendMessage(new TextMessage(jsonStr));
                    } catch (IOException e) {
                        log.error("SpringSocketHandle消息推送异常，客户端：{}, 用户: {}", session.getId(), msgModel.getFrom());
                    }
                });
    }

    public static String buildSendMessageModel(Object object) {
        return JSONUtil.toJsonStr(object);
    }

    public static ApiResult<Object> buildSendErrorMessageModel(ResultCode resultCode) {
        return ApiResult.fail(resultCode);
    }

}
