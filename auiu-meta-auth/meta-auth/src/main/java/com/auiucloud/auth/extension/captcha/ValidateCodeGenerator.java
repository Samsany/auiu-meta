package com.auiucloud.auth.extension.captcha;

import com.auiucloud.auth.domain.Captcha;

/**
 * @author dries
 **/
public interface ValidateCodeGenerator {

    Captcha generate();

}
