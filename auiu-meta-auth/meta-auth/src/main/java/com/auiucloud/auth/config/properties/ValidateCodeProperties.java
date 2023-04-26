package com.auiucloud.auth.config.properties;

import com.auiucloud.auth.enums.ImgCodeCharEnum;
import com.auiucloud.auth.enums.ImgCodeTypeEnum;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import java.util.ArrayList;
import java.util.List;

/**
 * @author dries
 **/
@Getter
@Setter
@Validated
@Component
@ConfigurationProperties(prefix = "meta.captcha")
public class ValidateCodeProperties {

    @NestedConfigurationProperty
    // 短信验证码
    private SmsCodeProperties sms = new SmsCodeProperties();
    @NestedConfigurationProperty
    // 验证码
    private ImgCodeProperties img = new ImgCodeProperties();

    @Getter
    @Setter
    public static class ImgCodeProperties {

        /**
         * 验证码类型
         */
        private ImgCodeTypeEnum type = ImgCodeTypeEnum.ARITHMETIC;

        /**
         * 验证码字符类型
         * 只有SpecCaptcha和GifCaptcha设置才有效果
         */
        private ImgCodeCharEnum charType = ImgCodeCharEnum.TYPE_DEFAULT;

        /**
         * 验证码长度
         */
        @Min(2)
        private Integer length = 2;

        /**
         * 宽度
         */
        private Integer width = 120;
        /**
         * 高度
         */
        private Integer height = 40;

        /**
         * 过期时间 s
         */
        @Min(60L)
        private long expireIn = 60;
        /**
         * 字体
         */
        @NestedConfigurationProperty
        private Font font = new Font();
        /**
         * 需要验证的url
         */
        private List<String> urls = new ArrayList<>();

        @Getter
        @Setter
        public static class Font {
            /**
             * 名称
             */
            private String name = "Arial";
            /**
             * 颜色
             */
            // private String color = "black";
            /**
             * 大小
             */
            private Integer size = 32;
        }
    }

    @Getter
    @Setter
    public static class SmsCodeProperties {

        /**
         * 短信验证码长度
         */
        @Min(2)
        private Integer length = 6;
        /**
         * 过期时间 (默认60s)
         */
        @Min(60L)
        private long expireIn = 60;
        /**
         * 需要验证的API
         */
        private List<String> urls = new ArrayList<>();

    }

}
