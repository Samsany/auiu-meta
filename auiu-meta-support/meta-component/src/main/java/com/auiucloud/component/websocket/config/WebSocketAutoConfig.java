package com.auiucloud.component.websocket.config;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import com.auiucloud.component.sd.service.IAiDrawService;
import com.auiucloud.component.websocket.handle.SpringSocketHandle;
import com.auiucloud.core.common.constant.Oauth2Constant;
import com.auiucloud.core.common.utils.SecurityUtil;
import com.auiucloud.core.rabbit.utils.RabbitMqUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.server.HandshakeInterceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * websocket 配置
 *
 * @author dries
 **/
@EnableWebSocket
@Configuration
public class WebSocketAutoConfig implements WebSocketConfigurer {

    private IAiDrawService aiDrawService;
    @Autowired
    public void setStreamBridge(IAiDrawService aiDrawService) {
        this.aiDrawService = aiDrawService;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(new SpringSocketHandle(aiDrawService), "/ws")
                .setAllowedOrigins("*")
                .addInterceptors(new MyHandshakeInterceptor());

        registry.addHandler(new SpringSocketHandle(aiDrawService), "/ws-sockjs")
                .setAllowedOriginPatterns("*")
                .addInterceptors(new MyHandshakeInterceptor())
                .withSockJS();
    }

    /**
     * 握手拦截器
     */
    private static class MyHandshakeInterceptor implements HandshakeInterceptor {
        @Override
        public boolean beforeHandshake(@NotNull ServerHttpRequest request, @NotNull ServerHttpResponse response,
                                       @NotNull WebSocketHandler handler, @NotNull Map<String, Object> attributes) throws Exception {
            HttpServletRequest servletRequest = ((ServletServerHttpRequest) request).getServletRequest();
            String token = servletRequest.getHeader(Oauth2Constant.JWT_TOKEN_HEADER);
            if (StrUtil.isNotBlank(token)) {
                if (StrUtil.isNotBlank(token)) {
                    token = token.replace(Oauth2Constant.JWT_TOKEN_PREFIX, "");
                } else {
                    return false;
                }
            } else {
                token = servletRequest.getParameter(Oauth2Constant.META_USER_TOKEN);
            }

            try {
                JSONObject jwtPayload = SecurityUtil.getJwtPayload(token);
                Long userId = jwtPayload.getLong(Oauth2Constant.META_USER_ID);
                if (ObjectUtil.isNull(userId)) {
                    return false;
                } else {
                    attributes.put(Oauth2Constant.META_USER_ID, userId);
                    attributes.put(Oauth2Constant.META_USER_TOKEN, token);
                    return true;
                }
            } catch (Exception e) {
                return false;
            }
        }

        @Override
        public void afterHandshake(@NotNull ServerHttpRequest request, @NotNull ServerHttpResponse response,
                                   @NotNull WebSocketHandler handler, Exception exception) {

        }

    }

}

