package com.auiucloud.auth.extension.douyin;

import cn.hutool.core.util.StrUtil;
import com.auiucloud.auth.model.AppletAuthCallback;
import com.auiucloud.auth.model.AppletUserInfo;
import com.auiucloud.auth.service.AppletAuthRequest;
import com.auiucloud.core.common.constant.Oauth2Constant;
import lombok.extern.slf4j.Slf4j;
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
public class DouyinTokenGranter extends AbstractTokenGranter {

    private final AuthenticationManager authenticationManager;
    private AppletAuthRequest appletAuthRequest;

    public DouyinTokenGranter(AuthenticationManager authenticationManager,
                              AuthorizationServerTokenServices tokenServices, ClientDetailsService clientDetailsService,
                              OAuth2RequestFactory requestFactory, AppletAuthRequest appletAuthRequest) {
        this(authenticationManager, tokenServices, clientDetailsService, requestFactory, Oauth2Constant.GRANT_TYPE_DOUYIN);
        this.appletAuthRequest = appletAuthRequest;
    }

    protected DouyinTokenGranter(AuthenticationManager authenticationManager, AuthorizationServerTokenServices tokenServices,
                                 ClientDetailsService clientDetailsService, OAuth2RequestFactory requestFactory, String grantType) {
        super(tokenServices, clientDetailsService, requestFactory, grantType);
        this.authenticationManager = authenticationManager;
    }

    @Override
    protected OAuth2Authentication getOAuth2Authentication(ClientDetails client, TokenRequest tokenRequest) {
        Map<String, String> parameters = new LinkedHashMap<>(tokenRequest.getRequestParameters());
        String appId = parameters.get("appId");
        String code = parameters.get("code");
        String encryptedData = parameters.get("encryptedData");
        String iv = parameters.get("iv");
        String rawUserInfo = parameters.get("userInfo");

        // 移除后续无用参数
        parameters.remove("code");
        parameters.remove("encryptedData");
        parameters.remove("iv");
        parameters.remove("userInfo");

        if (StrUtil.isBlank(appId) || StrUtil.isBlank(code) ) {
            throw new UserDeniedAuthorizationException("未传入请求参数");
        }
        String[] split = code.split("-");
        String oauthType = split[0];
        code = StrUtil.subSuf(code, oauthType.length() + 1);

        AppletAuthCallback authCallback = AppletAuthCallback.builder()
                .appId(appId)
                .code(code).encryptedData(encryptedData).iv(iv).rawUserInfo(rawUserInfo)
                .build();

        // 第三方登录
        AppletUserInfo appletUserInfo = appletAuthRequest.login(oauthType, authCallback);
        if (appletUserInfo == null) {
            throw new InvalidGrantException("Could not authenticate user.");
        }
        Authentication userAuth = new DouyinAuthenticationToken(appletUserInfo.getAccount()); // 未认证状态

        ((AbstractAuthenticationToken) userAuth).setDetails(parameters);
        try {
            userAuth = this.authenticationManager.authenticate(userAuth); // 认证中
        } catch (Exception e) {
            throw new InvalidGrantException(e.getMessage());
        }

        if (userAuth == null || !userAuth.isAuthenticated()) {
            throw new InvalidGrantException("Could not authenticate user.");
        }

        OAuth2Request storedOAuth2Request = getRequestFactory().createOAuth2Request(client, tokenRequest);
        return new OAuth2Authentication(storedOAuth2Request, userAuth);
    }
}
