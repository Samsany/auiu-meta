package com.auiucloud.auth.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONUtil;
import com.auiucloud.auth.model.*;
import com.auiucloud.auth.props.DouyinAppletProps;
import com.auiucloud.auth.service.DouyinAppletsService;
import com.auiucloud.auth.utils.AuthCryptUtils;
import com.auiucloud.core.common.constant.RedisKeyConstant;
import com.auiucloud.core.common.exception.ApiException;
import com.auiucloud.core.redis.core.RedisService;
import com.auiucloud.core.web.utils.RestTemplateUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * @author dries
 **/
@Slf4j
@RequiredArgsConstructor
public class DouyinAppletsServiceImpl implements DouyinAppletsService {

    @Resource
    private RedisService redisService;

    private AppletConfig appletConfig;

    public DouyinAppletsServiceImpl(AppletConfig appletConfig) {
        this.appletConfig = appletConfig;
    }

    @Override
    public String getAccessToken() {

        AppletAccessTokenInfo accessTokenResult = (AppletAccessTokenInfo) redisService.get(RedisKeyConstant.DOUYIN_APPLET_ACCESS_TOKEN);

        if (ObjectUtil.isNotNull(accessTokenResult)) {
            return accessTokenResult.getAccess_token();
        } else {
            // 设置请求头
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            // 设置请求参数
            Map<String, String> paramsMap = new HashMap<>();
            paramsMap.put("appid", appletConfig.getAppId());
            paramsMap.put("secret", appletConfig.getSecret());
            paramsMap.put("grant_type", "client_credential");
            HttpEntity<Map<String, String>> httpEntity = new HttpEntity<>(paramsMap, headers);

            DouyinAuthResult apiResponse = RestTemplateUtil.post(DouyinAppletProps.GET_ACCESS_TOKEN_URL_PROD, httpEntity, DouyinAuthResult.class).getBody();
            if (apiResponse != null && apiResponse.getErr_tips().equals("success")) {
                AppletAccessTokenInfo data = JSONUtil.toBean(JSONUtil.parseObj(apiResponse.getData()), AppletAccessTokenInfo.class);
                redisService.set(RedisKeyConstant.DOUYIN_APPLET_ACCESS_TOKEN, data, data.getExpires_in() / 2);
                return data.getAccess_token();
            }
            throw new ApiException("抖音授权错误，错误详情：【" + apiResponse.getErr_no() + ", " + apiResponse.getErr_tips() + "】");
        }

    }

    @Override
    public AppletCode2Session getCode2Session(String code) {
        // 设置请求头
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // 设置请求参数
        Map<String, String> paramsMap = new HashMap<>();
        paramsMap.put("appid", appletConfig.getAppId());
        paramsMap.put("secret", appletConfig.getSecret());
        paramsMap.put("code", code);
        paramsMap.put("anonymous_code", "");
        HttpEntity<Map<String, String>> httpEntity = new HttpEntity<>(paramsMap, headers);

        // ResponseEntity<DouyinApiResult> post = RestTemplateUtil.post(DouyinAppletConst.GET_CODE_SESSION_URL_PROD, httpEntity, DouyinApiResult.class);
        // DouyinApiResult apiResponse = post.getBody();
        DouyinAuthResult apiResponse = RestTemplateUtil.postObject(DouyinAppletProps.GET_CODE_SESSION_URL_PROD, httpEntity, DouyinAuthResult.class);
        // DouyinApiResult apiResponse = restTemplate.postForObject(DouyinAppletConst.GET_CODE_SESSION_URL_PROD, httpEntity, DouyinApiResult.class);
        if (apiResponse != null) {
            if (apiResponse.getErr_tips().equals("success")) {
                return JSONUtil.toBean(JSONUtil.parseObj(apiResponse.getData()), AppletCode2Session.class);
            }
            throw new ApiException("抖音授权错误，错误详情：【" + apiResponse.getErr_no() + ", " + apiResponse.getErr_tips() + "】");
        }
        throw new ApiException("抖音授权错误，网络异常");
    }

    @Override
    public AppletUserInfo getUserInfo(String sessionKey, String encryptedData, String iv) {
        return AppletUserInfo.fromJson(AuthCryptUtils.decrypt(encryptedData, sessionKey, iv));
    }

    public AppletConfig getAppletConfig() {
        return appletConfig;
    }

    public void setAppletConfig(AppletConfig appletConfig) {
        this.appletConfig = appletConfig;
    }
}
