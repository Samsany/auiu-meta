package com.auiucloud.auth.service;

import com.auiucloud.auth.domain.Captcha;

/**
 * 验证码业务类
 *
 * @author dries
 **/
public interface CaptchaService {

    /**
     * 渲染验证码
     *
     * @return 验证码内容
     */
    Captcha render();

    void validate();

}
