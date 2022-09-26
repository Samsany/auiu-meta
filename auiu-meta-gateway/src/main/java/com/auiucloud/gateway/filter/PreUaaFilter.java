package com.auiucloud.gateway.filter;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import com.auiucloud.core.cloud.props.MetaApiProperties;
import com.auiucloud.core.common.api.ApiResult;
import com.auiucloud.core.common.api.ResultCode;
import com.auiucloud.core.common.constant.Oauth2Constant;
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
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.List;

/**
 * @author dries
 * @date 2021/12/27
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class PreUaaFilter implements GlobalFilter, Ordered {

    private final RedisService redisService;
    private final MetaApiProperties metaApiProperties;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        ServerHttpRequest request = exchange.getRequest();
        URI uri = request.getURI();
        var pathMatcher = new AntPathMatcher();
        // 白名单路径移除JWT请求头
        List<String> ignoreUrls = metaApiProperties.getIgnoreUrl();
        for (String ignoreUrl : ignoreUrls) {
            if (pathMatcher.match(ignoreUrl, uri.getPath())) {
                request = exchange.getRequest().mutate().header(Oauth2Constant.JWT_TOKEN_HEADER, "").build();
                exchange = exchange.mutate().request(request).build();
            }
        }

        String token = request.getHeaders().getFirst(Oauth2Constant.JWT_TOKEN_HEADER);
        if (StrUtil.isBlank(token) || !StrUtil.startWithIgnoreCase(token, Oauth2Constant.JWT_TOKEN_PREFIX)) {
            return chain.filter(exchange);
        }

        try {
            // 从token中解析用户信息并设置到Header中去
            String realToken = token.replace(Oauth2Constant.JWT_TOKEN_PREFIX, "");
            JSONObject jwtPayload = SecurityUtil.getJwtPayload(realToken);
            log.info("AuthGlobalFilter.filter() user:{}", jwtPayload);
            String jti = jwtPayload.getStr(Oauth2Constant.META_USER_JTI);
            // 黑名单校验
            Boolean isBlack = redisService.hasKey(RedisKeyConstant.TOKEN_BLACKLIST_PREFIX + jti);
            if (isBlack) {
                return ResponseUtil.webFluxResponseWriter(exchange.getResponse(), StringPool.JSON_UTF8, HttpStatus.OK, ApiResult.fail(ResultCode.USER_ERROR_A0230));
            }
            request = exchange.getRequest().mutate()
                    .header(Oauth2Constant.META_USER, String.valueOf(jwtPayload))
                    .build();
            exchange = exchange.mutate().request(request).build();
        } catch (Exception e) {
            log.error("get token info fail", e);
        }

        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }
}
