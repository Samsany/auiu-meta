package com.auiucloud.gateway.auth;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;
import com.auiucloud.core.cloud.props.MetaApiProperties;
import com.auiucloud.core.common.constant.MetaConstant;
import com.auiucloud.core.common.constant.Oauth2Constant;
import com.auiucloud.core.common.constant.RedisKeyConstant;
import com.auiucloud.core.redis.core.RedisService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.ReactiveAuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.server.authorization.AuthorizationContext;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
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
@Component
@RequiredArgsConstructor
public class AuthorizationManager implements ReactiveAuthorizationManager<AuthorizationContext> {

    private final RedisService redisService;
    private final MetaApiProperties metaApiProperties;
    private final AntPathMatcher pathMatcher = new AntPathMatcher();

    @Override
    public Mono<AuthorizationDecision> check(Mono<Authentication> authentication, AuthorizationContext authorizationContext) {
        // 如果未启用网关验证，则跳过
        if (!metaApiProperties.isEnabled()) {
            return Mono.just(new AuthorizationDecision(true));
        }

        ServerHttpRequest request = authorizationContext.getExchange().getRequest();
        // 对应跨域的预检请求直接放行
        if (request.getMethod() == HttpMethod.OPTIONS) {
            return Mono.just(new AuthorizationDecision(true));
        }

        // 白名单路径直接放行
        URI uri = request.getURI();
        String path = uri.getPath();
        String restPath = request.getMethodValue() + "_" + uri.getPath();
        if (ignoreUrl(path)) {
            return Mono.just(new AuthorizationDecision(true));
        }

        // 未携带Token，未认证
        String token = request.getHeaders().getFirst(Oauth2Constant.JWT_TOKEN_HEADER);
        if (StrUtil.isEmpty(token)) {
            return Mono.just(new AuthorizationDecision(false));
        }

        // 非管理端路径直接放行
        if (!path.startsWith(RedisKeyConstant.ADMIN_URL_PATTERN)) {
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
     * @param path 路径
     * @return boolean
     */
    private boolean ignoreUrl(String path) {
        return metaApiProperties.getIgnoreUrl().stream()
                .anyMatch(url -> pathMatcher.match(url, path));
    }

}
