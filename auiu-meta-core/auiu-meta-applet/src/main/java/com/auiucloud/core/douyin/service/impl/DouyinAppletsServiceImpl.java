package com.auiucloud.core.douyin.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.auiucloud.core.common.constant.CommonConstant;
import com.auiucloud.core.common.constant.RedisKeyConstant;
import com.auiucloud.core.common.enums.IBaseEnum;
import com.auiucloud.core.common.exception.ApiException;
import com.auiucloud.core.douyin.enums.DouyinEnums;
import com.auiucloud.core.douyin.model.*;
import com.auiucloud.core.douyin.props.DouyinAppletProps;
import com.auiucloud.core.douyin.service.DouyinAppletsService;
import com.auiucloud.core.douyin.utils.AuthCryptUtils;
import com.auiucloud.core.redis.core.RedisService;
import com.auiucloud.core.web.utils.RestTemplateUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;

import java.util.ArrayList;
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

    private final RedisService redisService;

    private AppletConfig appletConfig;

    public DouyinAppletsServiceImpl(RedisService redisService, AppletConfig appletConfig) {
        this.redisService = redisService;
        this.appletConfig = appletConfig;
    }

    @Override
    public String getAccessToken() {

        DyAppletAccessTokenInfo accessTokenResult = (DyAppletAccessTokenInfo) redisService.get(RedisKeyConstant.DOUYIN_APPLET_ACCESS_TOKEN);

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

            DouyinApiResult apiResponse = RestTemplateUtil.post(DouyinAppletProps.GET_ACCESS_TOKEN_URL_PROD, httpEntity, DouyinApiResult.class).getBody();
            if (apiResponse != null && apiResponse.getErr_tips().equals("success")) {
                DyAppletAccessTokenInfo data = JSONUtil.toBean(JSONUtil.parseObj(apiResponse.getData()), DyAppletAccessTokenInfo.class);
                redisService.set(RedisKeyConstant.DOUYIN_APPLET_ACCESS_TOKEN, data, data.getExpires_in() / 2);
                return data.getAccess_token();
            }
            throw new ApiException("抖音授权错误，错误详情：【" + apiResponse.getErr_no() + ", " + apiResponse.getErr_tips() + "】");
        }

    }

    @Override
    public DyAppletCode2Session getCode2Session(String code) {
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
        DouyinApiResult apiResponse = RestTemplateUtil.postObject(DouyinAppletProps.GET_CODE_SESSION_URL_PROD, httpEntity, DouyinApiResult.class);
        // DouyinApiResult apiResponse = restTemplate.postForObject(DouyinAppletConst.GET_CODE_SESSION_URL_PROD, httpEntity, DouyinApiResult.class);
        if (apiResponse != null) {
            if (apiResponse.getErr_tips().equals("success")) {
                return JSONUtil.toBean(JSONUtil.parseObj(apiResponse.getData()), DyAppletCode2Session.class);
            }
            throw new ApiException("抖音授权错误，错误详情：【" + apiResponse.getErr_no() + ", " + apiResponse.getErr_tips() + "】");
        }
        throw new ApiException("抖音授权错误，网络异常");
    }

    @Override
    public AppletUserInfo getUserInfo(String sessionKey, String encryptedData, String iv) {
        return AppletUserInfo.fromJson(AuthCryptUtils.decrypt(encryptedData, sessionKey, iv));
    }

    /**
     * 预下单接口
     *
     * @return
     */
    @Override
    public DouyinApiResult createOrder() {
        return null;
    }

    @Override
    public boolean checkText(String content) {
        String accessToken = getAccessToken();
        // 设置请求头
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("X-Token", accessToken);

        // 设置请求参数
        JSONObject object = new JSONObject();
        object.set("content", content);
        MultiValueMap<String, Object> paramsMap = new LinkedMultiValueMap<>();
        paramsMap.put("tasks", List.of(object));

        try {
            HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<>(paramsMap, headers);
            DouyinTextAntidirtResult apiResponse = RestTemplateUtil.postObject(DouyinAppletProps.GET_TEXT_ANTIDIRT_URL_PROD, httpEntity, DouyinTextAntidirtResult.class);
            List<DouyinTextAntidirtResult.ResultData> data = apiResponse.getData();

            for (DouyinTextAntidirtResult.ResultData datum : data) {
                if (datum.getCode().equals(CommonConstant.STATUS_NORMAL_VALUE)) {
                    List<DouyinTextAntidirtResult.Predicts> predicts = datum.getPredicts();
                    for (DouyinTextAntidirtResult.Predicts predict : predicts) {
                        return predict.getHit();
                    }
                }
            }
        } catch (RestClientException e) {
            throw new ApiException("抖音内容安全检测接口异常，验证失败");
        }
        return false;
    }

    @Override
    public List<Integer> checkTextList(List<String> contents) {
        String accessToken = getAccessToken();
        // 设置请求头
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("X-Token", accessToken);

        // 设置请求参数
        List<Object> objList = new ArrayList<>();
        for (String content : contents) {
            if (StrUtil.isNotBlank(content)) {
                JSONObject obj = JSONUtil.createObj();
                obj.set("content", content);
                objList.add(obj);
            }
        }

        List<Integer> errIndex = new ArrayList<>();
        if (CollUtil.isNotEmpty(objList)) {
            MultiValueMap<String, Object> paramsMap = new LinkedMultiValueMap<>();
            paramsMap.put("tasks", objList);

            try {
                HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<>(paramsMap, headers);
                DouyinTextAntidirtResult apiResponse = RestTemplateUtil.postObject(DouyinAppletProps.GET_TEXT_ANTIDIRT_URL_PROD, httpEntity, DouyinTextAntidirtResult.class);
                List<DouyinTextAntidirtResult.ResultData> data = apiResponse.getData();

                int index = 0;
                for (DouyinTextAntidirtResult.ResultData datum : data) {
                    if (datum.getCode().equals(CommonConstant.STATUS_NORMAL_VALUE)) {
                        List<DouyinTextAntidirtResult.Predicts> predicts = datum.getPredicts();
                        for (DouyinTextAntidirtResult.Predicts predict : predicts) {
                            if (predict.getHit()) {
                                errIndex.add(index);
                                index++;
                            }
                        }
                    }
                }
            } catch (Exception e) {
                throw new ApiException("抖音内容安全检测接口异常，请重试");
            }
        }

        return errIndex;
    }

    @Override
    public String checkImage(String image) {
        // 设置请求参数
        Map<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("app_id", appletConfig.getAppId());

        String accessToken = getAccessToken();
        paramsMap.put("access_token", accessToken);
        // 检测的图片链接
        paramsMap.put("image", image);
        // 图片数据的 base64 格式，有 image 字段时，此字段无效
        // paramsMap.put("image_data", image);

        return checkImage(paramsMap);
    }

    @Override
    public String checkBase64Image(String imageData) {
        // 设置请求参数
        Map<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("app_id", appletConfig.getAppId());
        String accessToken = getAccessToken();
        paramsMap.put("access_token", accessToken);
        // 检测的图片链接
        // paramsMap.put("image", image);
        // 图片数据的 base64 格式，有 image 字段时，此字段无效
        paramsMap.put("image_data", imageData);
        return checkImage(paramsMap);
    }

    private String checkImage(Map<String, Object> paramsMap) {
        // 设置请求头
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> httpEntity = new HttpEntity<>(paramsMap, headers);
        DouyinImgDetectionResult apiResponse = RestTemplateUtil.postObject(DouyinAppletProps.GET_IMAGE_DETECTION_V2_PROD_URL, httpEntity, DouyinImgDetectionResult.class);
        if (apiResponse.getError().equals(CommonConstant.STATUS_NORMAL_VALUE)) {
            List<DouyinImgDetectionResult.Predict> predicts = apiResponse.getPredicts();
            List<DouyinImgDetectionResult.Predict> predictList = predicts.parallelStream()
                    .filter(DouyinImgDetectionResult.Predict::getHit).toList();
            String msg = "";
            if (CollUtil.isNotEmpty(predictList)) {
                msg = predictList.parallelStream().map(it -> {
                    String modelName = it.getModel_name();
                    DouyinEnums.ImageDetection enumByValue = IBaseEnum.getEnumByValue(modelName, DouyinEnums.ImageDetection.class);
                    return enumByValue.getLabel();
                }).collect(Collectors.joining(","));
            }

            log.debug("抖音图片安全检测：{}", msg);
            return msg;
        }

        throw new ApiException("抖音图片安全检测接口异常,请重试");
    }

    public AppletConfig getAppletConfig() {
        return appletConfig;
    }

    public void setAppletConfig(AppletConfig appletConfig) {
        this.appletConfig = appletConfig;
    }
}
