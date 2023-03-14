package com.auiucloud.auth.service.impl;

import com.auiucloud.auth.service.SmsCodeSenderService;
import com.auiucloud.core.common.exception.ApiException;

/**
 * @author dries
 **/
public class DefaultSmsCodeSenderImpl implements SmsCodeSenderService {

    /**
     * 发送验证码
     */
    @Override
    public void send(String key, String mobile, String params) {
        throw new ApiException("请配置默认的短信发送器");
    }

}
