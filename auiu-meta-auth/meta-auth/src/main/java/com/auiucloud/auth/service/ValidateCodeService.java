package com.auiucloud.auth.service;

import com.auiucloud.auth.domain.Captcha;
import com.auiucloud.auth.enums.ValidateCodeTypeEnum;
import com.auiucloud.core.common.exception.CaptchaException;
import org.springframework.web.context.request.ServletWebRequest;

import javax.naming.AuthenticationException;

/**
 * @author dries
 **/
public interface ValidateCodeService {

    /***
     * save 保存验证码
     **/
    void save(Captcha code, ValidateCodeTypeEnum validateCodeType);

    /***
     * get 获取验证码
     **/
    Captcha get(ValidateCodeTypeEnum validateCodeType, Captcha captcha);

    /***
     * remove 移除验证码
     **/
    void remove(ValidateCodeTypeEnum validateCodeType, Captcha captcha);

}
