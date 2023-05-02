package com.auiucloud.auth.extension.captcha;

import com.auiucloud.auth.domain.Captcha;
import org.springframework.web.context.request.ServletWebRequest;

/**
 * @author dries
 **/
public interface ValidateCodeGenerator {

    Captcha generate();

}
