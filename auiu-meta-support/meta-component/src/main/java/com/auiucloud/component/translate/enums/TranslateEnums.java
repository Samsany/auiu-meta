package com.auiucloud.component.translate.enums;

import com.auiucloud.core.common.enums.IBaseEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author dries
 **/
public class TranslateEnums {

    @Getter
    @AllArgsConstructor
    public enum Type implements IBaseEnum<String> {

        BAIDU_TRANSLATION("百度翻译", "baidu_translation"),
        ;

        private final String label;
        private final String value;
    }

    @Getter
    @AllArgsConstructor
    public enum Lang implements IBaseEnum<String> {

        AUTO("auto", "自动检测"),
        ZH("zh", "中文"),
        EN("en", "英文"),
        ;

        private final String value;
        private final String label;

    }

}
