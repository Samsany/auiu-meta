package com.auiucloud.auth.extension.captcha.sms;

import cn.hutool.json.JSONObject;
import com.auiucloud.auth.domain.Captcha;
import com.auiucloud.auth.extension.captcha.AbstractValidateCodeProcessor;
import com.auiucloud.core.common.constant.Oauth2Constant;
import com.auiucloud.core.common.constant.RedisKeyConstant;
import com.auiucloud.core.common.utils.http.RequestHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author dries
 **/
@Slf4j
@Component("smsCodeProcessor")
@RequiredArgsConstructor
public class SmsCodeProcessor extends AbstractValidateCodeProcessor<Captcha> {
    /**
     * 发送校验码，由子类实现
     *
     * @param captcha
     */
    @Override
    protected void send(Captcha captcha) {
        String mobile = RequestHolder.getHttpServletRequestHeader(Oauth2Constant.DEFAULT_PARAMETER_NAME_MOBILE);
        String key = RequestHolder.getHttpServletRequestHeader(Oauth2Constant.DEVICE_HEADER_KEY);
        JSONObject obj = new JSONObject();
        obj.putOpt("code", captcha.getCode());
        log.info("发送验证码=======》{}", captcha.getCode());
        // smsCodeSenderService.send(key, mobile, String.valueOf(obj));
    }
}
