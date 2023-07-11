package com.auiucloud.component.websocket.utils;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.auiucloud.core.common.api.ApiResult;
import com.auiucloud.core.common.api.ResultCode;
import com.auiucloud.core.common.model.WsMsgModel;
import com.auiucloud.core.common.utils.StringPool;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
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

    @SuppressWarnings("all")
    public static void sendMessage(WebSocketSession session, String message) throws IOException {
        if (ObjectUtil.isNotNull(session) && session.isOpen()) {
            synchronized (session) {
                session.sendMessage(new TextMessage(message));
            }
        } else {
            log.error("推送消息异常：消息：{}, 通道：{}", message, session);
        }
    }

    public static void sendMessage(ConcurrentHashMap<String, WebSocketSession> userMap, WsMsgModel msgModel) throws IOException {
        if (CollUtil.isNotEmpty(userMap)) {
            for (WebSocketSession session : userMap.values()) {
                String message = JSONUtil.toJsonStr(msgModel);
                sendMessage(session, message);
            }
        }
    }

    public static void sendMessageToUser(WsMsgModel msgModel) throws IOException {
        ConcurrentHashMap<String, WebSocketSession> userMap = users.get(msgModel.getTo());
        sendMessage(userMap, msgModel);
    }

    public static void sendMessageToInUser(WsMsgModel msgModel) throws IOException {
        String to = msgModel.getTo();
        if (StrUtil.isNotBlank(to)) {
            String[] userIds = to.split(StringPool.COMMA);
            for (String userId : userIds) {
                ConcurrentHashMap<String, WebSocketSession> userMap = users.get(userId);
                msgModel.setTo(userId);
                sendMessage(userMap, msgModel);
            }
        }
    }

    public static void sendMessageToExUser(WsMsgModel msgModel) throws IOException {
        String to = msgModel.getTo();
        if (StrUtil.isNotBlank(to)) {
            List<String> userIds = Arrays.asList(to.split(StringPool.COMMA));
            for (Map.Entry<String, ConcurrentHashMap<String, WebSocketSession>> mapEntry : users.entrySet()) {
                if (!userIds.contains(mapEntry.getKey())) {
                    ConcurrentHashMap<String, WebSocketSession> userMap = mapEntry.getValue();
                    msgModel.setTo(mapEntry.getKey());
                    sendMessage(userMap, msgModel);
                }
            }
        }
    }

    public static void sendMessageToAll(WsMsgModel msgModel) throws IOException {
        for (Map.Entry<String, ConcurrentHashMap<String, WebSocketSession>> mapEntry : users.entrySet()) {
            ConcurrentHashMap<String, WebSocketSession> userMap = mapEntry.getValue();
            msgModel.setTo(mapEntry.getKey());
            sendMessage(userMap, msgModel);
        }
    }

    public static ApiResult<Object> buildSendErrorMessageModel(ResultCode resultCode) {
        return ApiResult.fail(resultCode);
    }

}
