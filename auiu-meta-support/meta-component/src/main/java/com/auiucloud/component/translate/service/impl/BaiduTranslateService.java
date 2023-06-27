package com.auiucloud.component.translate.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.URLUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.json.JSONUtil;
import com.auiucloud.component.translate.component.TranslateService;
import com.auiucloud.component.translate.domain.BaiduTextTranslateResult;
import com.auiucloud.component.translate.domain.TextTranslateParams;
import com.auiucloud.component.translate.domain.TranslateProperties;
import com.auiucloud.component.translate.domain.TranslateResult;
import com.auiucloud.core.common.api.ApiResult;
import com.auiucloud.core.common.api.ResultCode;
import com.auiucloud.core.common.constant.CommonConstant;
import com.auiucloud.core.common.utils.StringPool;
import com.auiucloud.core.web.utils.RestTemplateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author dries
 **/
@Slf4j
public class BaiduTranslateService extends TranslateService {

    private final String TEXT_TRANSLATION_URL = "https://fanyi-api.baidu.com/api/trans/vip/translate";

    public BaiduTranslateService(TranslateProperties properties) {
        this.properties = properties;
    }

    /**
     * 通用文本翻译API
     */
    @Override
    public ApiResult<TranslateResult> textTranslation(TextTranslateParams params) {
        // 组装参数
        try {
            String salt = RandomUtil.randomString(10);
            String str1 = properties.getAppId()
                    .concat(params.getContent())
                    .concat(salt)
                    .concat(properties.getAppSecret());

            String sign = SecureUtil.md5(str1);

            MultiValueMap<String, Object> paramsMap = new LinkedMultiValueMap<>();
            paramsMap.set("q", params.getContent());
            paramsMap.set("from", params.getFrom());
            paramsMap.set("to", params.getTo());
            paramsMap.set("appid", properties.getAppId());
            paramsMap.set("salt", salt);
            paramsMap.set("sign", sign);

            // 设置请求头
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            BaiduTextTranslateResult result = RestTemplateUtil.post(TEXT_TRANSLATION_URL, headers, paramsMap, BaiduTextTranslateResult.class).getBody();
            if (ObjectUtil.isNull(result)) {
                return ApiResult.fail(ResultCode.ERROR);
            } else {
                if (ObjectUtil.isNotNull(result.getError_code())) {
                    log.error("百度文本翻译请求异常， 错误码{}, 异常信息：{}", result.getError_code(), result.getError_msg());
                    return ApiResult.fail(result.getError_msg());
                }
                List<BaiduTextTranslateResult.TransResult> transResult = result.getTrans_result();
                String src = transResult.parallelStream()
                        .map(BaiduTextTranslateResult.TransResult::getSrc)
                        .collect(Collectors.joining(StringPool.NEWLINE));
                String dst = transResult.parallelStream()
                        .map(BaiduTextTranslateResult.TransResult::getDst)
                        .collect(Collectors.joining(StringPool.NEWLINE));
                return ApiResult.data(TranslateResult.builder()
                        .from(result.getFrom())
                        .to(result.getTo())
                        .src(src)
                        .dst(dst)
                        .build());
            }
        } catch (Exception e) {
            log.error("百度文本翻译请求异常，异常信息：{}", e.getMessage());
            return ApiResult.fail(e.getMessage());
        }
    }
}
