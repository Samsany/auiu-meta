package com.auiucloud.auth.controller;

import com.auiucloud.auth.domain.Captcha;
import com.auiucloud.auth.extension.captcha.ValidateCodeProcessorHolder;
import com.auiucloud.core.common.api.ApiResult;
import com.auiucloud.core.common.constant.Oauth2Constant;
import com.auiucloud.core.log.annotation.Log;
import com.xkcoding.justauth.AuthRequestFactory;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.zhyd.oauth.model.AuthCallback;
import me.zhyd.oauth.request.AuthRequest;
import me.zhyd.oauth.utils.AuthStateUtils;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.endpoint.TokenEndpoint;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.Principal;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Oauth2授权接口
 *
 * @author dries
 * @date 2022/4/8
 */
@Slf4j
@RestController
@Tag(name = "Oauth2管理")
@RequiredArgsConstructor
@RequestMapping("/oauth")
public class Oauth2Controller {

    private final TokenEndpoint tokenEndpoint;
    private final SocialConfig socialConfig;
    private final AuthRequestFactory factory;
    private final ValidateCodeProcessorHolder validateCodeProcessorHolder;

    @Log(value = "用户登录", exception = "用户登录请求异常")
    @Operation(summary = "用户登录Get")
    @Parameters({
            @Parameter(name = "grant_type", description = "授权模式", in = ParameterIn.QUERY, required = true),
            @Parameter(name = "refresh_token", description = "刷新token", in = ParameterIn.QUERY),
            @Parameter(name = "username", description = "用户名", in = ParameterIn.QUERY),
            @Parameter(name = "password", description = "密码", in = ParameterIn.QUERY),
            @Parameter(name = "scope", description = "使用范围", in = ParameterIn.QUERY)
    })
    @GetMapping("/token")
    public ApiResult<?> getAccessToken(Principal principal, @RequestParam Map<String, String> parameters) throws HttpRequestMethodNotSupportedException {
        return custom(tokenEndpoint.getAccessToken(principal, parameters).getBody());
    }

    @Log(value = "用户登录", exception = "用户登录请求异常")
    @Operation(summary = "用户登录Post")
    @Parameters({
            @Parameter(name = "grant_type", description = "授权模式", in = ParameterIn.QUERY, required = true),
            @Parameter(name = "refresh_token", description = "刷新token", in = ParameterIn.QUERY),
            @Parameter(name = "username", description = "用户名", in = ParameterIn.QUERY),
            @Parameter(name = "password", description = "密码", in = ParameterIn.QUERY),
            @Parameter(name = "scope", description = "使用范围", in = ParameterIn.QUERY)
    })
    @PostMapping("/token")
    public ApiResult<?> postAccessToken(Principal principal, @RequestBody Map<String, String> parameters) throws HttpRequestMethodNotSupportedException {
        return custom(tokenEndpoint.postAccessToken(principal, parameters).getBody());
    }

    /**
     * 返回用户登录信息
     *
     * @param oAuth2AccessToken token
     * @return ApiResult
     */
    private ApiResult<?> custom(OAuth2AccessToken oAuth2AccessToken) {
        Map<String, Object> data = new LinkedHashMap<>(oAuth2AccessToken.getAdditionalInformation());
        data.put("accessToken", oAuth2AccessToken.getValue());
        data.put("bearerType", Oauth2Constant.JWT_TOKEN_PREFIX);
        data.put("expiresIn", oAuth2AccessToken.getExpiresIn());
        if (oAuth2AccessToken.getRefreshToken() != null) {
            data.put("refreshToken", oAuth2AccessToken.getRefreshToken().getValue());
        }
        return ApiResult.data(data);
    }

    /**
     * 登录
     *
     * @param oauthType 第三方登录类型
     * @param response  response
     * @throws IOException IO异常
     */
    @Log(value = "第三方登录", exception = "第三方登录请求异常")
    @Operation(summary = "第三方登录", description = "第三方登录")
    @GetMapping("/login/{oauthType}")
    public void login(@PathVariable String oauthType, HttpServletResponse response) throws IOException {
        AuthRequest authRequest = factory.get(oauthType);
        response.sendRedirect(authRequest.authorize(oauthType + "::" + AuthStateUtils.createState()));
    }

    /**
     * 登录成功后的回调
     *
     * @param oauthType 第三方登录类型
     * @param callback  携带返回的信息
     */
    @Log(value = "第三方登录回调", exception = "第三方登录回调请求异常")
    @Operation(summary = "第三方登录回调", description = "第三方登录回调")
    @GetMapping("/callback/{oauthType}")
    public void callback(@PathVariable String oauthType, AuthCallback callback, HttpServletResponse httpServletResponse) throws IOException {
        String url = socialConfig.getUrl() + "?code=" + oauthType + "-" + callback.getCode() + "&state=" + callback.getState();
        log.debug("url:{}", url);
        //跳转到指定页面
        httpServletResponse.sendRedirect(url);
    }

    /**
     * 创建验证码，根据验证码类型不同，调用不同的 {@link Captcha}接口实现
     */
    @Operation(summary = "发送验证码", description = "发送验证码")
    @Parameters({
            @Parameter(name = "type", description = "验证码类型：sms | image", in = ParameterIn.PATH, required = true)
    })
    @GetMapping("/captcha/{type}")
    public ApiResult<?> createCode(@PathVariable String type) {
        Captcha captcha = validateCodeProcessorHolder.findValidateCodeProcessor(type)
                .render();
        return ApiResult.data(captcha);
    }

}
