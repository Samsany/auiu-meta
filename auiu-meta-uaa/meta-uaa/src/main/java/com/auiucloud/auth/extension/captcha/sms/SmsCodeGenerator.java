package com.auiucloud.auth.extension.captcha.sms;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.RandomUtil;
import com.auiucloud.auth.config.properties.ValidateCodeProperties;
import com.auiucloud.auth.domain.Captcha;
import com.auiucloud.auth.extension.captcha.ValidateCodeGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @author dries
 **/
@Component("smsValidateCodeGenerator")
@RequiredArgsConstructor
public class SmsCodeGenerator implements ValidateCodeGenerator {
    private final ValidateCodeProperties validateCodeProperties;

    @Override
    public Captcha generate() {
        String code = RandomUtil.randomNumbers(validateCodeProperties.getSms().getLength());
        return Captcha.builder()
                .uuid(IdUtil.simpleUUID())
                .code(code)
                .expireTime(Date.from(new Date().toInstant().plusSeconds(validateCodeProperties.getSms().getExpireIn())))
                .build();
    }
}
