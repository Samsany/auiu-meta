package com.auiucloud.auth.extension.social;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSON;
import com.auiucloud.core.common.constant.Oauth2Constant;
import com.auiucloud.core.common.constant.RedisKeyConstant;
import com.auiucloud.core.redis.core.RedisService;
import com.xkcoding.justauth.AuthRequestFactory;
import lombok.extern.slf4j.Slf4j;
import me.zhyd.oauth.enums.AuthResponseStatus;
import me.zhyd.oauth.model.AuthCallback;
import me.zhyd.oauth.model.AuthResponse;
import me.zhyd.oauth.model.AuthUser;
import me.zhyd.oauth.request.AuthRequest;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.common.exceptions.InvalidGrantException;
import org.springframework.security.oauth2.common.exceptions.UserDeniedAuthorizationException;
import org.springframework.security.oauth2.provider.*;
import org.springframework.security.oauth2.provider.token.AbstractTokenGranter;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author dries
 **/
@Slf4j
public class SocialTokenGranter extends AbstractTokenGranter {

    private final AuthenticationManager authenticationManager;

    private RedisService redisService;

    private AuthRequestFactory factory;

    public SocialTokenGranter(AuthenticationManager authenticationManager,
                              AuthorizationServerTokenServices tokenServices, ClientDetailsService clientDetailsService,
                              OAuth2RequestFactory requestFactory, RedisService redisService, AuthRequestFactory factory) {
        this(authenticationManager, tokenServices, clientDetailsService, requestFactory, Oauth2Constant.GRANT_TYPE_SOCIAL);
        this.redisService = redisService;
        this.factory = factory;
    }

    protected SocialTokenGranter(AuthenticationManager authenticationManager, AuthorizationServerTokenServices tokenServices,
                                 ClientDetailsService clientDetailsService, OAuth2RequestFactory requestFactory, String grantType) {
        super(tokenServices, clientDetailsService, requestFactory, grantType);
        this.authenticationManager = authenticationManager;
    }

    @Override
    protected OAuth2Authentication getOAuth2Authentication(ClientDetails client, TokenRequest tokenRequest) {
        Map<String, String> parameters = new LinkedHashMap<>(tokenRequest.getRequestParameters());
        String code = parameters.get("code");
        String state = parameters.get("state");

        if (StrUtil.isBlank(code)) {
            throw new UserDeniedAuthorizationException("未传入请求参数");
        }

        String codeFromRedis = redisService.get(RedisKeyConstant.SOCIAL_CACHE_PREFIX + state).toString();
        if (codeFromRedis == null) {
            throw new UserDeniedAuthorizationException("授权已过期,请重新发起授权请求");
        }

        String[] split = code.split("-");
        String oauthType = split[0];
        code = StrUtil.subSuf(code, oauthType.length() + 1);

        AuthRequest authRequest = factory.get(oauthType);
        AuthCallback authCallback = AuthCallback.builder().code(code).state(state).build();
        AuthResponse response = authRequest.login(authCallback);
        log.info("【response】= {}", JSON.toJSON(response));
        // 第三方登录成功
        AuthUser authUser = null;
        if (response.getCode() == AuthResponseStatus.SUCCESS.getCode()) {
            authUser = (AuthUser) response.getData();
        }
        log.error("authUser:{}", JSON.toJSON(authUser));
        Authentication userAuth = new SocialAuthenticationToken(authUser);

        ((AbstractAuthenticationToken) userAuth).setDetails(parameters);
        try {
            userAuth = authenticationManager.authenticate(userAuth);
        } catch (Exception ase) {
            // covers expired, locked, disabled cases (mentioned in section 5.2, draft 31)
            throw new InvalidGrantException(ase.getMessage());
        }
        // If the username/password are wrong the spec says we should send 400/invalid grant

        if (userAuth == null || !userAuth.isAuthenticated()) {
            throw new InvalidGrantException("Could not authenticate user.");
        }

        OAuth2Request storedOAuth2Request = getRequestFactory().createOAuth2Request(client, tokenRequest);
        return new OAuth2Authentication(storedOAuth2Request, userAuth);
    }
}
