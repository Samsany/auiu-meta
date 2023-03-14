package com.auiucloud.component.enums;

import com.auiucloud.core.common.enums.IBaseEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author dries
 **/
@Getter
@AllArgsConstructor
public enum ConfigTypeEnum implements IBaseEnum<String> {

    BASE_SETTING("基础信息配置", "system"),
    OSS_SETTING("OSS资源配置", "oss"),
    ;

    private final String label;
    private final String value;
}
