package com.auiucloud.auth.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.auiucloud.auth.config.properties.ValidateCodeProperties;
import com.auiucloud.auth.domain.Captcha;
import com.auiucloud.auth.enums.ValidateCodeTypeEnum;
import com.auiucloud.auth.service.ValidateCodeService;
import com.auiucloud.core.common.constant.RedisKeyConstant;
import com.auiucloud.core.common.exception.CaptchaException;
import com.auiucloud.core.redis.core.RedisService;
import lombok.RequiredArgsConstructor;

import javax.annotation.Resource;

/**
 * @author dries
 **/
@RequiredArgsConstructor
public class RedisValidateCodeServiceImpl implements ValidateCodeService {

    @Resource
    private RedisService redisService;
    @Resource
    private ValidateCodeProperties validateProperties;

    /***
     * save 保存验证码
     */
    @Override
    public void save(Captcha captcha, ValidateCodeTypeEnum validateCodeType) {
        long expireIn;
        if (validateCodeType.equals(ValidateCodeTypeEnum.IMAGE)) {
            expireIn = validateProperties.getImg().getExpireIn();
        } else {
            expireIn = validateProperties.getSms().getExpireIn();
        }

        redisService.set(buildKey(validateCodeType, captcha), captcha, expireIn);
    }

    /***
     * get 获取验证码
     */
    @Override
    public Captcha get(ValidateCodeTypeEnum validateCodeType, Captcha captcha) {
        Object value = redisService.get(buildKey(validateCodeType, captcha));
        if (value == null) {
            return null;
        }
        return (Captcha) value;
    }

    /***
     * remove 移除验证码
     */
    @Override
    public void remove(ValidateCodeTypeEnum validateCodeType, Captcha captcha) {
        redisService.del(buildKey(validateCodeType, captcha));
    }

    private String buildKey(ValidateCodeTypeEnum codeType, Captcha captcha) {

        if (ObjectUtil.isNull(captcha)) {
            throw new CaptchaException("参数校验异常");
        }

        if (codeType.equals(ValidateCodeTypeEnum.SMS)) {
            return RedisKeyConstant.SMS_CODE_KEY + captcha.getUuid();
        } else {
            return RedisKeyConstant.CAPTCHA_KEY + captcha.getUuid();
        }
    }
}
