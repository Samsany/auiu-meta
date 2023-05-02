package com.auiucloud.auth.extension.social;

import com.alibaba.fastjson2.JSON;
import com.auiucloud.core.common.constant.Oauth2Constant;
import com.auiucloud.core.common.enums.MethodTypeEnum;
import com.xkcoding.justauth.AuthRequestFactory;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import me.zhyd.oauth.enums.AuthResponseStatus;
import me.zhyd.oauth.model.AuthCallback;
import me.zhyd.oauth.model.AuthResponse;
import me.zhyd.oauth.model.AuthUser;
import me.zhyd.oauth.request.AuthRequest;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.util.Assert;

import java.io.IOException;

/**
 * @author dries
 */
@Slf4j
public class SocialAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    private boolean postOnly;

    /**
     * 请求中的参数
     */
    private String socialParameter = Oauth2Constant.DEFAULT_PARAMETER_NAME_SOCIAL;

    private AuthRequestFactory authRequestFactory;

    /**
     * 通过构造函数指定该 Filter 要拦截的 url 和 httpMethod
     */
    protected SocialAuthenticationFilter() {
        super(new AntPathRequestMatcher(Oauth2Constant.OAUTH_SOCIAL, null));
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        if (postOnly && !MethodTypeEnum.POST.name().equals(request.getMethod())) {
            throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
        }
        SocialAuthenticationToken token;

        token = new SocialAuthenticationToken(obtainAuthUser(request));
        this.setDetails(request, token);

        // 3. 返回 authenticated 方法的返回值
        return this.getAuthenticationManager().authenticate(token);
    }

    /**
     * 获取手机号
     */
    protected String obtainSocial(HttpServletRequest request) {
        return request.getParameter(socialParameter);
    }

    protected void setDetails(HttpServletRequest request, SocialAuthenticationToken authRequest) {
        authRequest.setDetails(authenticationDetailsSource.buildDetails(request));
    }

    public void setSocialParameter(String socialParameter) {
        Assert.hasText(socialParameter, "Social parameter must not be empty or null");
        this.socialParameter = socialParameter;
    }

    /**
     * 获取 just-auth 登录后的用户信息
     */
    protected AuthUser obtainAuthUser(HttpServletRequest request) {
        String type = getCallbackType(request);
        AuthRequest authRequest = authRequestFactory.get(type);

        // 登录后，从第三方拿到用户信息
        AuthResponse response = authRequest.login(getCallback(request));
        log.info("【justauth 第三方登录 response】= {}", JSON.toJSON(response));
        // 第三方登录成功
        if (response.getCode() == AuthResponseStatus.SUCCESS.getCode()) {
            return (AuthUser) response.getData();
        }
        return null;
    }

    /**
     * 从请求中构建 AuthCallback
     */
    private AuthCallback getCallback(HttpServletRequest request) {

        AuthCallback authCallback = AuthCallback.builder()
                .code(request.getParameter("code"))
                .auth_code(request.getParameter("auth_code"))
                .authorization_code(request.getParameter("authorization_code"))
                .oauth_token(request.getParameter("oauth_token"))
                .state(request.getParameter("state"))
                .oauth_verifier(request.getParameter("oauth_verifier"))
                .build();

        return authCallback;
    }

    /**
     * 获取路径参数：回调类型
     */
    private String getCallbackType(HttpServletRequest request) {
        // /context/open/oauth/callback/gitee
        String uri = request.getRequestURI();
        // "/open/oauth/callback/".length()
        int common = Oauth2Constant.OAUTH_SOCIAL.length() - 2;
        int start = uri.indexOf(Oauth2Constant.OAUTH_SOCIAL.substring(0, common));
        if (start == -1) {
            log.warn("【JustAuth 第三方登录 response】回调类型为空，uri={}", uri);
            return null;
        }
        // gitee
        return uri.substring(start + common);
    }

    public final String getSocialParameter() {
        return socialParameter;
    }

    public void setPostOnly(boolean postOnly) {
        this.postOnly = postOnly;
    }

    public void setAuthRequestFactory(AuthRequestFactory authRequestFactory) {
        this.authRequestFactory = authRequestFactory;
    }
}
