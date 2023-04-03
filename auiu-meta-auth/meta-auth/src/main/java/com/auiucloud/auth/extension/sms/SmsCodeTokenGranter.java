package com.auiucloud.auth.extension.sms;

import cn.hutool.core.util.StrUtil;
import com.auiucloud.auth.extension.sms.SmsCodeAuthenticationToken;
import com.auiucloud.core.common.constant.Oauth2Constant;
import com.auiucloud.core.common.constant.RedisKeyConstant;
import com.auiucloud.core.redis.core.RedisService;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.AccountStatusException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
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
public class SmsCodeTokenGranter extends AbstractTokenGranter {

    private final AuthenticationManager authenticationManager;

    private RedisService redisService;

    public SmsCodeTokenGranter(AuthenticationManager authenticationManager,
                               AuthorizationServerTokenServices tokenServices,
                               ClientDetailsService clientDetailsService,
                               OAuth2RequestFactory requestFactory,
                               RedisService redisService) {
        this(authenticationManager, tokenServices, clientDetailsService, requestFactory, Oauth2Constant.GRANT_TYPE_SMS);
        this.redisService = redisService;
    }

    protected SmsCodeTokenGranter(AuthenticationManager authenticationManager, AuthorizationServerTokenServices tokenServices,
                                  ClientDetailsService clientDetailsService, OAuth2RequestFactory requestFactory, String grantType) {
        super(tokenServices, clientDetailsService, requestFactory, grantType);
        this.authenticationManager = authenticationManager;
    }

    @Override
    protected OAuth2Authentication getOAuth2Authentication(ClientDetails client, TokenRequest tokenRequest) {
        Map<String, String> parameters = new LinkedHashMap<>(tokenRequest.getRequestParameters());
        String mobile = parameters.get(Oauth2Constant.DEFAULT_PARAMETER_NAME_MOBILE);
        String code = parameters.get("code");

        if (StrUtil.isBlank(code)) {
            throw new UserDeniedAuthorizationException("请输入验证码！");
        }

        String codeFromRedis = null;
        // 从Redis里读取存储的验证码信息
        try {
            codeFromRedis = redisService.get(RedisKeyConstant.SMS_CODE_KEY + mobile).toString();
        } catch (Exception e) {
            throw new UserDeniedAuthorizationException("验证码不存在！");
        }

        if (codeFromRedis == null) {
            throw new UserDeniedAuthorizationException("验证码已过期！");
        }
        // 比较输入的验证码是否正确
        if (!StrUtil.equalsIgnoreCase(code, codeFromRedis)) {
            throw new UserDeniedAuthorizationException("验证码不正确！");
        }

        redisService.del(RedisKeyConstant.SMS_CODE_KEY + mobile);

        Authentication userAuth = new SmsCodeAuthenticationToken(mobile);
        ((AbstractAuthenticationToken) userAuth).setDetails(parameters);
        try {
            userAuth = authenticationManager.authenticate(userAuth);
        } catch (AccountStatusException | BadCredentialsException ase) {
            //covers expired, locked, disabled cases (mentioned in section 5.2, draft 31)
            throw new InvalidGrantException(ase.getMessage());
        }
        // If the username/password are wrong the spec says we should send 400/invalid grant

        if (userAuth == null || !userAuth.isAuthenticated()) {
            throw new InvalidGrantException("Could not authenticate user: " + mobile);
        }

        OAuth2Request storedOAuth2Request = getRequestFactory().createOAuth2Request(client, tokenRequest);
        return new OAuth2Authentication(storedOAuth2Request, userAuth);
    }
}