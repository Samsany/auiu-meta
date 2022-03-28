package com.auiucloud.uaa.controller;

import com.auiucloud.core.common.api.ApiResult;
import com.auiucloud.core.log.annotation.Log;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.endpoint.TokenEndpoint;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

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
@Api(tags = "Oauth2管理")
@RequiredArgsConstructor
@RequestMapping("/oauth")
public class Oauth2Controller {

    private final TokenEndpoint tokenEndpoint;
    // private final RedisService redisService;

    @Log(value = "用户登录", exception = "用户登录请求异常")
    @ApiOperation("用户登录Get")
    @GetMapping("/token")
    public ApiResult<?> getAccessToken(@ApiIgnore Principal principal, @ApiIgnore @RequestParam Map<String, String> parameters) throws HttpRequestMethodNotSupportedException {
        return custom(tokenEndpoint.postAccessToken(principal, parameters).getBody());
    }

    @Log(value = "用户登录", exception = "用户登录请求异常")
    @ApiOperation("用户登录Post")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "grant_type", value = "授权模式", paramType = "query", dataTypeClass = String.class, required = true),
//            @ApiImplicitParam(name = "client_id", value = "Oauth2客户端ID", paramType = "query", dataTypeClass = String.class),
//            @ApiImplicitParam(name = "client_secret", value = "Oauth2客户端秘钥", paramType = "query", dataTypeClass = String.class),
            @ApiImplicitParam(name = "refresh_token", value = "刷新token", paramType = "query", dataTypeClass = String.class),
            @ApiImplicitParam(name = "username", value = "用户名", paramType = "query", dataTypeClass = String.class),
            @ApiImplicitParam(name = "password", value = "密码", paramType = "query", dataTypeClass = String.class),
            @ApiImplicitParam(name = "scope", value = "使用范围", paramType = "query", dataTypeClass = String.class)
    })
    @PostMapping("/token")
    public ApiResult<?> postAccessToken(@ApiIgnore Principal principal, @ApiIgnore @RequestParam Map<String, String> parameters) throws HttpRequestMethodNotSupportedException {
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
        if (oAuth2AccessToken.getRefreshToken() != null) {
            data.put("refreshToken", oAuth2AccessToken.getRefreshToken().getValue());
        }
        return ApiResult.data(data);
    }

}
