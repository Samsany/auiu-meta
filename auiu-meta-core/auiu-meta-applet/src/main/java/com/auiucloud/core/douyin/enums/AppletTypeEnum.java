package com.auiucloud.core.douyin.enums;

import com.auiucloud.core.common.enums.IBaseEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author dries
 **/
@Getter
@AllArgsConstructor
public enum AppletTypeEnum implements IBaseEnum<String> {

    WECHAT("wechat", "微信小程序"),
    DOUYIN("douyin", "抖音小程序"),
    ;

    private final String value;
    private final String label;

}
