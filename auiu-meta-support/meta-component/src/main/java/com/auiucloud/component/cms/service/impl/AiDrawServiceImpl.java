package com.auiucloud.component.cms.service.impl;

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.auiucloud.component.cms.domain.SdText2ImgConfig;
import com.auiucloud.component.cms.props.SdConstants;
import com.auiucloud.component.cms.service.IAiDrawService;
import com.auiucloud.core.common.api.ApiResult;
import com.auiucloud.core.common.utils.GsonUtil;
import com.auiucloud.core.common.utils.SecurityUtil;
import com.auiucloud.core.web.utils.RestTemplateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.HashMap;
import java.util.Map;

/**
 * AI 绘画实现类
 *
 * @author dries
 **/
@Slf4j
@Service
public class AiDrawServiceImpl implements IAiDrawService {

    private final static String url = "http://192.168.0.111:7860";

    @Override
    public ApiResult<?> sdText2Img(SdText2ImgConfig config) {
        Long userId = SecurityUtil.getUserId();
        // 设置请求头
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        Object data = RestTemplateUtil.post(url + SdConstants.text2Img, headers, config, Object.class).getBody();
        return ApiResult.data(data);
    }

}
