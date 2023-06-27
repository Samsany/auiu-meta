package com.auiucloud.component.translate.component;

import com.auiucloud.component.translate.domain.TextTranslateParams;
import com.auiucloud.component.translate.domain.TranslateProperties;
import com.auiucloud.component.translate.domain.TranslateResult;
import com.auiucloud.core.common.api.ApiResult;

/**
 * @author dries
 **/
public abstract class TranslateService {

    /**
     * 机器配置属性
     */
    protected TranslateProperties properties;

    /**
     * 通用文本翻译API
     */
    public abstract ApiResult<TranslateResult> textTranslation(TextTranslateParams params);

}
