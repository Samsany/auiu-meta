package com.auiucloud.auth.security.captcha;

import com.auiucloud.auth.domain.Captcha;
import com.auiucloud.auth.enums.ValidateCodeTypeEnum;
import com.auiucloud.auth.service.CaptchaService;
import com.auiucloud.core.common.constant.Oauth2Constant;
import com.auiucloud.core.common.exception.CaptchaException;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Map;

/**
 * 校验码处理器，封装不同校验码的处理逻辑
 *
 * @author dries
 **/
@Component
public class ValidateCodeProcessorHolder {

    @Resource
    private Map<String, CaptchaService> validateCodeProcessorMap;

    public CaptchaService findValidateCodeProcessor(ValidateCodeTypeEnum codeType) {
        return findValidateCodeProcessor(codeType.toString().toLowerCase());
    }

    public CaptchaService findValidateCodeProcessor(String type) {
        // String name = type.toLowerCase() + Captcha.class.getSimpleName();
        String name = buildKey(type);
        CaptchaService processor = validateCodeProcessorMap.get(name);
        if (processor == null) {
            throw new CaptchaException("验证码处理器" + name + "不存在");
        }
        return processor;
    }

    private static String buildKey(String type) {
        return type.toLowerCase() + Oauth2Constant.CAPTCHA_PROCESSOR_SEPARATOR;
    }

}
