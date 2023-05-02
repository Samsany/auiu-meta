package com.auiucloud.auth.extension.captcha.image;

import cn.hutool.core.util.IdUtil;
import com.auiucloud.auth.config.properties.ValidateCodeProperties;
import com.auiucloud.auth.domain.Captcha;
import com.auiucloud.auth.enums.ImgCodeCharEnum;
import com.auiucloud.auth.enums.ImgCodeTypeEnum;
import com.auiucloud.auth.extension.captcha.ValidateCodeGenerator;
import com.wf.captcha.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @author dries
 **/
@Component("imageValidateCodeGenerator")
@RequiredArgsConstructor
public class ImageCodeGenerator implements ValidateCodeGenerator {

    private final ValidateCodeProperties validateCodeProperties;

    @Override
    public Captcha generate() {
        ValidateCodeProperties.ImgCodeProperties img = validateCodeProperties.getImg();

        ImgCodeTypeEnum type = img.getType();
        ImgCodeCharEnum charType = img.getCharType();
        // ValidateCodeProperties.ImgCodeProperties.Font font = img.getFont();

        Integer length = img.getLength();
        Integer width = img.getWidth();
        Integer height = img.getHeight();
        long expireIn = img.getExpireIn();

        String code = "";
        String content = "";
        switch (type) {
            case SPECC -> {
                // png类型
                // 三个参数分别为宽、高、位数
                SpecCaptcha specCaptcha = new SpecCaptcha(width, height, length);
                // 设置字体 有默认字体，可以不用设置
                // specCaptcha.setFont(new Font("Verdana", Font.PLAIN, font.getSize()));
                // charType 设置类型，纯数字、纯字母、字母数字混合
                switch (charType) {
                    case TYPE_DEFAULT -> specCaptcha.setCharType(com.wf.captcha.base.Captcha.TYPE_DEFAULT);
                    case TYPE_ONLY_CHAR -> specCaptcha.setCharType(com.wf.captcha.base.Captcha.TYPE_ONLY_CHAR);
                    case TYPE_ONLY_LOWER -> specCaptcha.setCharType(com.wf.captcha.base.Captcha.TYPE_ONLY_LOWER);
                    case TYPE_ONLY_UPPER -> specCaptcha.setCharType(com.wf.captcha.base.Captcha.TYPE_ONLY_UPPER);
                    case TYPE_ONLY_NUMBER -> specCaptcha.setCharType(com.wf.captcha.base.Captcha.TYPE_ONLY_NUMBER);
                    case TYPE_NUM_AND_UPPER -> specCaptcha.setCharType(com.wf.captcha.base.Captcha.TYPE_NUM_AND_UPPER);
                }
                code = specCaptcha.text();
                content = specCaptcha.toBase64();
            }
            case GIF -> {
                // gif类型
                GifCaptcha gifCaptcha = new GifCaptcha(width, height, length);
                // charType 设置类型，纯数字、纯字母、字母数字混合
                switch (charType) {
                    case TYPE_DEFAULT -> gifCaptcha.setCharType(com.wf.captcha.base.Captcha.TYPE_DEFAULT);
                    case TYPE_ONLY_CHAR -> gifCaptcha.setCharType(com.wf.captcha.base.Captcha.TYPE_ONLY_CHAR);
                    case TYPE_ONLY_LOWER -> gifCaptcha.setCharType(com.wf.captcha.base.Captcha.TYPE_ONLY_LOWER);
                    case TYPE_ONLY_UPPER -> gifCaptcha.setCharType(com.wf.captcha.base.Captcha.TYPE_ONLY_UPPER);
                    case TYPE_ONLY_NUMBER -> gifCaptcha.setCharType(com.wf.captcha.base.Captcha.TYPE_ONLY_NUMBER);
                    case TYPE_NUM_AND_UPPER -> gifCaptcha.setCharType(com.wf.captcha.base.Captcha.TYPE_NUM_AND_UPPER);
                }
                code = gifCaptcha.text();
                content = gifCaptcha.toBase64();
            }
            case CHINESE -> {
                // 中文类型
                ChineseCaptcha chineseCaptcha = new ChineseCaptcha(width, height, length);
                code = chineseCaptcha.text();
                content = chineseCaptcha.toBase64();
            }
            case CHINESE_GIF -> {
                // 中文gif类型
                ChineseGifCaptcha chineseGifCaptcha = new ChineseGifCaptcha(width, height, length);
                code = chineseGifCaptcha.text();
                content = chineseGifCaptcha.toBase64();
            }
            case ARITHMETIC -> {
                // 算术类型
                ArithmeticCaptcha arithmeticCaptcha = new ArithmeticCaptcha(width, height, length);
                arithmeticCaptcha.getArithmeticString();  // 获取运算的公式：3+2=?
                // 获取运算的结果：5
                code = arithmeticCaptcha.text();
                // 输出内容
                content = arithmeticCaptcha.toBase64();
            }
        }


        return Captcha.builder()
                .uuid(IdUtil.simpleUUID())
                .code(code)
                .content(content)
                .expireTime(Date.from(new Date().toInstant().plusSeconds(expireIn)))
                .build();
    }

}
