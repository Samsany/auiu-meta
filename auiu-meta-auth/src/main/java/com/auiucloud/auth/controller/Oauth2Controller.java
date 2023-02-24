package com.auiucloud.auth.controller;

import com.auiucloud.core.common.api.ApiResult;
import com.auiucloud.core.common.constant.Oauth2Constant;
import com.auiucloud.core.log.annotation.Log;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.endpoint.TokenEndpoint;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Oauth2授权接口
 *
 * @author dries
 * @date 2022/4/8
 */
@RestController
@Tag(name = "Oauth2管理")
@RequiredArgsConstructor
@RequestMapping("/oauth")
public class Oauth2Controller {

    private final TokenEndpoint tokenEndpoint;
    // private final RedisService redisService;

    @Log(value = "用户登录", exception = "用户登录请求异常")
    @Operation(summary ="用户登录Get")
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
    @Operation(summary ="用户登录Post")
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

}
