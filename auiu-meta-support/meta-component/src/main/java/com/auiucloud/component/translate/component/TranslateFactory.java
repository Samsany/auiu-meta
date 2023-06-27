package com.auiucloud.component.translate.component;

import com.auiucloud.component.translate.domain.TranslateProperties;
import com.auiucloud.component.translate.enums.TranslateEnums;
import com.auiucloud.component.translate.service.ITranslateConfigService;
import com.auiucloud.component.translate.service.impl.BaiduTranslateService;
import com.auiucloud.core.common.enums.IBaseEnum;
import com.auiucloud.core.web.utils.SpringContextUtils;
import lombok.extern.slf4j.Slf4j;

/**
 * @author dries
 **/
@Slf4j
public final class TranslateFactory {

    private static ITranslateConfigService translateConfigService;

    static {
        TranslateFactory.translateConfigService = SpringContextUtils.getBean("translateConfigService");
    }

    public static TranslateService build() {
        // 获取机器翻译配置信息
        TranslateProperties properties = translateConfigService.getDefaultTranslationProperties();
        TranslateEnums.Type type = IBaseEnum.getEnumByValue(properties.getCode(), TranslateEnums.Type.class);

        // log.info("获取机器翻译默认配置 {}", type);
        return switch (type) {
            case BAIDU_TRANSLATION -> new BaiduTranslateService(properties);
        };
    }

}
