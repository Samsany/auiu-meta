package com.auiucloud.auth.security.captcha.image;

import com.auiucloud.auth.domain.Captcha;
import com.auiucloud.auth.security.captcha.AbstractValidateCodeProcessor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;

/**
 * @author dries
 **/
@Slf4j
@Component("imageCodeProcessor")
@RequiredArgsConstructor
public class ImageCodeProcessor extends AbstractValidateCodeProcessor<Captcha> {

    /**
     * 发送校验码
     */
    @Override
    protected void send(Captcha captcha) {
//        if (response != null) {
//            try {
//                // 过滤 code expireTime
//                captcha.setCode(null);
//                captcha.setExpireTime(null);
//                ResponseUtil.responseWriter(
//                        response,
//                        MediaType.APPLICATION_JSON_VALUE,
//                        HttpStatus.OK.value(),
//                        ApiResult.data(captcha)
//                );
//            } catch (IOException e) {
//                throw new CaptchaException("验证码获取异常");
//            }
//        }
    }

}
