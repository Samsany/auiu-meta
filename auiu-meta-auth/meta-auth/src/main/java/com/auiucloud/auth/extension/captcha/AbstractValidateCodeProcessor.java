package com.auiucloud.auth.extension.captcha;

import cn.hutool.core.util.StrUtil;
import com.auiucloud.auth.domain.Captcha;
import com.auiucloud.auth.enums.ValidateCodeTypeEnum;
import com.auiucloud.auth.service.CaptchaService;
import com.auiucloud.auth.service.ValidateCodeService;
import com.auiucloud.core.common.constant.Oauth2Constant;
import com.auiucloud.core.common.constant.RedisKeyConstant;
import com.auiucloud.core.common.exception.CaptchaException;
import com.auiucloud.core.common.utils.http.RequestHolder;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @author dries
 **/
@Slf4j
public abstract class AbstractValidateCodeProcessor<C extends Captcha> implements CaptchaService {

    /**
     * 收集系统中所有的 {@link ValidateCodeGenerator} 接口的实现。
     */
    @Resource
    private Map<String, ValidateCodeGenerator> validateCodeGeneratorMap;
    @Resource
    private ValidateCodeService validateCodeService;

    /**
     * 渲染验证码
     *
     * @return 验证码内容
     */
    @Override
    public Captcha render() {
        C validateCode = generate();
        save(validateCode);
        send(validateCode);

        return validateCode
                .withCode(null)
                .withExpireTime(null);
    }

    @Override
    public void validate() {
        ValidateCodeTypeEnum codeType = getValidateCodeType();
        String key = RequestHolder.getHttpServletRequestHeader(Oauth2Constant.DEVICE_HEADER_KEY);
        String code = RequestHolder.getHttpServletRequestHeader(Oauth2Constant.DEVICE_HEADER_CODE);
        Captcha captcha = Captcha.builder()
                .uuid(key)
                .code(code)
                .build();
        C codeFromRedis = (C) validateCodeService.get(codeType, captcha);

        if (StrUtil.isBlank(code)) {
            throw new CaptchaException("请输入验证码");
        }
        if (codeFromRedis == null) {
            throw new CaptchaException("验证码已过期");
        }

        if (!StrUtil.equalsIgnoreCase(code, codeFromRedis.toString())) {
            throw new CaptchaException("验证码不正确");
        }
    }

    /**
     * 生成校验码
     */
    @SuppressWarnings("unchecked")
    private C generate() {
        String type = getValidateCodeType().toString().toLowerCase();
        String generatorName = type + ValidateCodeGenerator.class.getSimpleName();
        ValidateCodeGenerator validateCodeGenerator = validateCodeGeneratorMap.get(generatorName);
        if (validateCodeGenerator == null) {
            throw new CaptchaException("验证码生成器" + generatorName + "不存在");
        }
        return (C) validateCodeGenerator.generate();
    }

    /**
     * 保存校验码
     */
    private void save(C validateCode) {
        // log.info("保存效验码==========》{}", validateCode.getCode());
        validateCodeService.save(validateCode.withContent(null), getValidateCodeType());
    }

    /**
     * 根据请求的url获取校验码的类型
     */
    private ValidateCodeTypeEnum getValidateCodeType() {
        String type = StrUtil.subBefore(
                getClass().getSimpleName(),
                Oauth2Constant.CAPTCHA_PROCESSOR_SEPARATOR,
                false
        );
        return ValidateCodeTypeEnum.valueOf(type.toUpperCase());
    }

    /**
     * 发送校验码，由子类实现
     */
    protected abstract void send(C validateCode);

}
