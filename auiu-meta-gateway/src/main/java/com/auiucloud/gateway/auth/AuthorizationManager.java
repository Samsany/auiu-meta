package com.auiucloud.gateway.auth;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import com.auiucloud.core.cloud.props.MetaApiProperties;
import com.auiucloud.core.common.constant.MetaConstant;
import com.auiucloud.core.common.constant.Oauth2Constant;
import com.auiucloud.core.common.constant.ProviderConstant;
import com.auiucloud.core.common.constant.RedisKeyConstant;
import com.auiucloud.core.common.utils.SecurityUtil;
import com.auiucloud.core.common.utils.StringPool;
import com.auiucloud.core.redis.core.RedisService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.ReactiveAuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.server.authorization.AuthorizationContext;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * 网关鉴权管理器，用于判断是否有资源的访问权限
 *
 * @author dries
 * @date 2022/2/8
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class AuthorizationManager implements ReactiveAuthorizationManager<AuthorizationContext> {

    private final RedisService redisService;
    private final MetaApiProperties metaApiProperties;
    private final AntPathMatcher pathMatcher = new AntPathMatcher();

    @Override
    public Mono<AuthorizationDecision> check(Mono<Authentication> authentication, AuthorizationContext authorizationContext) {

        // 如果未启用网关验证，则跳过
        if (!metaApiProperties.getEnabled()) {
            return Mono.just(new AuthorizationDecision(true));
        }

        ServerWebExchange exchange = authorizationContext.getExchange();
        ServerHttpRequest request = exchange.getRequest();
        // 对应跨域的预检请求直接放行
        if (request.getMethod() == HttpMethod.OPTIONS) {
            return Mono.just(new AuthorizationDecision(true));
        }

        // 白名单路径直接放行
        URI uri = request.getURI();
        String path = replacePrefix(request.getURI().getPath());
        String rawPath = request.getURI().getRawPath();
        String restPath = request.getMethodValue() + "_" + uri.getPath();

        if (ignoreUrl(path) || ignoreUrl(rawPath)) {
            // 移除请求头
            // if (!pathMatcher.match(Oauth2Constant.OAUTH_ALL, path)) {
            //     request = request.mutate().headers(httpHeaders -> httpHeaders.remove(Oauth2Constant.JWT_TOKEN_HEADER)).build();
            // }

            exchange.mutate().request(request).build();
            return Mono.just(new AuthorizationDecision(true));
        }

        // 未携带Token，未认证
        String token = request.getHeaders().getFirst(Oauth2Constant.JWT_TOKEN_HEADER);
        if (StrUtil.isBlank(token) || !StrUtil.startWithIgnoreCase(token, Oauth2Constant.JWT_TOKEN_PREFIX)) {
            return Mono.just(new AuthorizationDecision(false));
        }

        try {
            // 从token中解析用户信息并设置到Header中去
            String realToken = token.replace(Oauth2Constant.JWT_TOKEN_PREFIX, "");
            JSONObject jwtPayload = SecurityUtil.getJwtPayload(realToken);
            log.info("AuthGlobalFilter.filter() user:{}", jwtPayload);
            String jti = jwtPayload.getStr(Oauth2Constant.META_USER_JTI);
            // 黑名单校验
            Boolean isBlack = redisService.hasKey(RedisKeyConstant.TOKEN_BLACKLIST_PREFIX + jti);
            if (Boolean.TRUE.equals(isBlack)) {
                return Mono.just(new AuthorizationDecision(false));
            }
            request = exchange.getRequest().mutate()
                    .header(Oauth2Constant.META_USER, String.valueOf(jwtPayload))
                    .build();
            exchange.mutate()
                    .request(request)
                    .build();
        } catch (Exception e) {
            log.error("get token info fail", e);
            return Mono.just(new AuthorizationDecision(false));
        }

        // 非管理端路径直接放行
        if (!rawPath.startsWith(Oauth2Constant.ADMIN_URL_PATTERN)) {
            return Mono.just(new AuthorizationDecision(true));
        }

        Set<String> authorities = new HashSet<>();
        // 管理端路径需校验权限
        if (needCheckAuth(restPath, authorities)) {
            return authentication.filter(Authentication::isAuthenticated)
                    .flatMapIterable(Authentication::getAuthorities)
                    .map(GrantedAuthority::getAuthority)
                    // 角色中只要有一个满足则通过权限校验
                    .any(authority -> authority.equals(MetaConstant.SUPER_ADMIN_CODE) || authorities.contains(authority))
                    .map(AuthorizationDecision::new)
                    .defaultIfEmpty(new AuthorizationDecision(false));
        } else {
            return Mono.just(new AuthorizationDecision(true));
        }
    }

    /**
     * 是否需要鉴权
     *
     * @param restPath 路径
     * @return true ｜ false
     */
    private boolean needCheckAuth(String restPath, Set<String> authorities) {
        // 是否被设置需要鉴权
        boolean needCheck = false;
        var permRolesRule = redisService.hGetAll(RedisKeyConstant.URL_PERM_ROLES_KEY);
        for (Map.Entry<Object, Object> permRoles : permRolesRule.entrySet()) {
            String perm = (String) permRoles.getKey();
            if (pathMatcher.match(perm, restPath)) {
                authorities.addAll(Convert.toList(String.class, permRoles.getValue()));
                needCheck = true;
            }
        }
        return needCheck;
    }

    /**
     * 检查是否忽略url
     *
     * @param path 路径
     * @return boolean
     */
    private boolean ignoreUrl(String path) {
        return metaApiProperties.getIgnoreUrls().parallelStream()
                .anyMatch(url -> pathMatcher.match(url, path));
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

}
