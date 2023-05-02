package com.auiucloud.auth.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONUtil;
import com.auiucloud.auth.model.*;
import com.auiucloud.auth.props.DouyinAppletProps;
import com.auiucloud.auth.service.DouyinAppletsService;
import com.auiucloud.auth.utils.AuthCryptUtils;
import com.auiucloud.core.common.constant.CommonConstant;
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
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    @Override
    public boolean checkText(String content) {
        String accessToken = getAccessToken();
        // 设置请求头
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("X-Token", accessToken);

        Map<String, String> map = new HashMap<>();
        map.put("content", content);
        // 设置请求参数
        Map<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("tasks", List.of(map));

        HttpEntity<Map<String, Object>> httpEntity = new HttpEntity<>(paramsMap, headers);
        TextAntidirtResult apiResponse = RestTemplateUtil.postObject(DouyinAppletProps.GET_TEXT_ANTIDIRT_URL_PROD, httpEntity, TextAntidirtResult.class);
        List<TextAntidirtResult.ResultData> data = apiResponse.getData();

        for (TextAntidirtResult.ResultData datum : data) {
            if (datum.getCode().equals(CommonConstant.STATUS_NORMAL_VALUE)) {
                List<TextAntidirtResult.Predicts> predicts = datum.getPredicts();
                for (TextAntidirtResult.Predicts predict : predicts) {
                    if (predict.getHit()) {
                        // throw new ApiException("内容包含敏感信息，请修改");
                        return false;
                    }
                }
            }
        }
        return true;
    }

    @Override
    public boolean checkImage(String image) {
        String accessToken = getAccessToken();
        // 设置请求头
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // 设置请求参数
        Map<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("app_id", appletConfig.getAppId());
        paramsMap.put("access_token", accessToken);
        // 检测的图片链接
        paramsMap.put("image", image);
        // 图片数据的 base64 格式，有 image 字段时，此字段无效
        // paramsMap.put("image_data", image);

        HttpEntity<Map<String, Object>> httpEntity = new HttpEntity<>(paramsMap, headers);
        ImageDetectionResult apiResponse = RestTemplateUtil.postObject(DouyinAppletProps.GET_IMAGE_DETECTION_V2__PROD_URL, httpEntity, ImageDetectionResult.class);
        if (apiResponse.getError().equals(CommonConstant.STATUS_NORMAL_VALUE)) {
            List<ImageDetectionResult.Predict> predicts = apiResponse.getPredicts();
            List<ImageDetectionResult.Predict> predictList = predicts.parallelStream().filter(ImageDetectionResult.Predict::getHit).collect(Collectors.toList());
            return !CollUtil.isNotEmpty(predictList);
        }

        return false;
    }

    public AppletConfig getAppletConfig() {
        return appletConfig;
    }

    public void setAppletConfig(AppletConfig appletConfig) {
        this.appletConfig = appletConfig;
    }
}
