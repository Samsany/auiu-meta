package com.auiucloud.auth.extension.captcha;

import cn.hutool.core.util.StrUtil;
import com.auiucloud.auth.domain.Captcha;
import com.auiucloud.core.common.constant.Oauth2Constant;
import com.auiucloud.core.common.constant.RedisKeyConstant;
import com.auiucloud.core.common.utils.http.RequestHolder;
import com.auiucloud.core.redis.core.RedisService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.common.exceptions.InvalidGrantException;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.security.oauth2.common.exceptions.UserDeniedAuthorizationException;
import org.springframework.security.oauth2.provider.*;
import org.springframework.security.oauth2.provider.token.AbstractTokenGranter;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author dries
 **/
public class CaptchaTokenGranter extends AbstractTokenGranter {

    private final AuthenticationManager authenticationManager;

    private RedisService redisService;

    public CaptchaTokenGranter(AuthenticationManager authenticationManager,
                               AuthorizationServerTokenServices tokenServices, ClientDetailsService clientDetailsService,
                               OAuth2RequestFactory requestFactory, RedisService redisService) {
        this(authenticationManager, tokenServices, clientDetailsService, requestFactory, Oauth2Constant.GRANT_TYPE_CAPTCHA);
        this.redisService = redisService;
    }

    protected CaptchaTokenGranter(AuthenticationManager authenticationManager, AuthorizationServerTokenServices tokenServices,
                                  ClientDetailsService clientDetailsService, OAuth2RequestFactory requestFactory, String grantType) {
        super(tokenServices, clientDetailsService, requestFactory, grantType);
        this.authenticationManager = authenticationManager;
    }

    @Override
    protected OAuth2Authentication getOAuth2Authentication(ClientDetails client, TokenRequest tokenRequest) {
        HttpServletRequest request = RequestHolder.getHttpServletRequest();

        if (null == request) {
            throw new OAuth2Exception("请求参数不存在！");
        }
        // 增加验证码判断
        String key = request.getHeader(Oauth2Constant.DEVICE_HEADER_KEY);
        String code = request.getHeader(Oauth2Constant.DEVICE_HEADER_CODE);
        Captcha codeFromRedis = null;
        try {
            codeFromRedis = (Captcha) redisService.get(RedisKeyConstant.CAPTCHA_KEY + key);
        } catch (Exception e) {
            throw new UserDeniedAuthorizationException("验证码已过期");
        }

        if (StrUtil.isBlank(code)) {
            throw new UserDeniedAuthorizationException("请输入验证码");
        }
        if (codeFromRedis == null) {
            throw new UserDeniedAuthorizationException("验证码已过期");
        }
        if (!StrUtil.equalsIgnoreCase(code, codeFromRedis.getCode())) {
            throw new UserDeniedAuthorizationException("验证码不正确");
        }
        // 验证成功，移除验证码
        redisService.del(RedisKeyConstant.CAPTCHA_KEY + key);

        Map<String, String> parameters = new LinkedHashMap<String, String>(tokenRequest.getRequestParameters());
        String username = parameters.get("username");
        String password = parameters.get("password");
        // Protect from downstream leaks of password
        parameters.remove("password");

        Authentication userAuth = new UsernamePasswordAuthenticationToken(username, password);
        ((AbstractAuthenticationToken) userAuth).setDetails(parameters);
        try {
            userAuth = authenticationManager.authenticate(userAuth);
        } catch (AccountStatusException | BadCredentialsException ase) {
            //covers expired, locked, disabled cases (mentioned in section 5.2, draft 31)
            throw new InvalidGrantException(ase.getMessage());
        }
        // If the username/password are wrong the spec says we should send 400/invalid grant

        if (userAuth == null || !userAuth.isAuthenticated()) {
            throw new InvalidGrantException("Could not authenticate user: " + username);
        }

        OAuth2Request storedOAuth2Request = getRequestFactory().createOAuth2Request(client, tokenRequest);
        return new OAuth2Authentication(storedOAuth2Request, userAuth);
    }
}
