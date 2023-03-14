package com.auiucloud.gateway.filter;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import com.auiucloud.core.cloud.props.MetaApiProperties;
import com.auiucloud.core.common.api.ApiResult;
import com.auiucloud.core.common.api.ResultCode;
import com.auiucloud.core.common.constant.Oauth2Constant;
import com.auiucloud.core.common.constant.ProviderConstant;
import com.auiucloud.core.common.constant.RedisKeyConstant;
import com.auiucloud.core.common.utils.SecurityUtil;
import com.auiucloud.core.common.utils.StringPool;
import com.auiucloud.core.common.utils.http.ResponseUtil;
import com.auiucloud.core.redis.core.RedisService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @author dries
 * @date 2021/12/27
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class PreAuthFilter implements GlobalFilter, Ordered {

    private final RedisService redisService;
    private final MetaApiProperties metaApiProperties;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        // 如果未启用网关验证，则跳过
        if (!metaApiProperties.isEnabled()) {
            return chain.filter(exchange);
        }

        ServerHttpRequest request = exchange.getRequest();
        // 白名单路径跳过并移除JWT请求头
        String path = replacePrefix(request.getURI().getPath());
        String requestUrl = request.getURI().getRawPath();
        if (ignoreUrl(path) || ignoreUrl(requestUrl)) {
            request = request.mutate().header(Oauth2Constant.JWT_TOKEN_HEADER, "").build();
            exchange = exchange.mutate().request(request).build();
            return chain.filter(exchange);
        }

        // 验证token是否有效
        ServerHttpResponse response = exchange.getResponse();
        String token = request.getHeaders().getFirst(Oauth2Constant.JWT_TOKEN_HEADER);
        if (StrUtil.isBlank(token) || !StrUtil.startWithIgnoreCase(token, Oauth2Constant.JWT_TOKEN_PREFIX)) {
            return unauthorized(response, "用户未登录！");
        }

        try {
            // 从token中解析用户信息并设置到Header中去
            // String realToken = token.replace(Oauth2Constant.JWT_TOKEN_PREFIX, "");
            JSONObject jwtPayload = SecurityUtil.getJwtPayload();
            log.info("AuthGlobalFilter.filter() user:{}", jwtPayload);
            String jti = jwtPayload.getStr(Oauth2Constant.META_USER_JTI);
            // 黑名单校验
            Boolean isBlack = redisService.hasKey(RedisKeyConstant.TOKEN_BLACKLIST_PREFIX + jti);
            if (Boolean.TRUE.equals(isBlack)) {
                return unauthorized(response, "登录超时，请重新登录");
            }
            request = exchange.getRequest().mutate()
                    .header(Oauth2Constant.META_USER, String.valueOf(jwtPayload))
                    .build();
            exchange = exchange.mutate()
                    .request(request)
                    .build();
        } catch (Exception e) {
            log.error("get token info fail", e);
            return unauthorized(response, "用户未登录或登录已过期！");
        }

        return chain.filter(exchange);
    }

    /**
     * 检查是否忽略url
     *
     * @param path 路径
     * @return boolean
     */
    private boolean ignoreUrl(String path) {
        return metaApiProperties.getIgnoreUrls().stream()
                .map(url -> url.replace("/**", ""))
                .anyMatch(path::startsWith);
    }

    /**
     * 移除模块前缀
     *
     * @param path 路径
     * @return String
     */
    private String replacePrefix(String path) {
        if (path.startsWith(ProviderConstant.MODULE_PATH_PREFIX)) {
            return path.substring(path.indexOf(StringPool.SLASH, 1));
        }
        return path;
    }

    private Mono<Void> unauthorized(ServerHttpResponse resp, String msg) {
        return ResponseUtil.webFluxResponseWriter(resp, StringPool.JSON_UTF8, HttpStatus.OK, ApiResult.fail(ResultCode.USER_ERROR_A0230.getCode(), msg));
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }
}
