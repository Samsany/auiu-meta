package com.auiucloud.core.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 1：用户名密码登录　2：手机号登录　3：社交登录
 *
 * @author dries
 **/
@Getter
@AllArgsConstructor
public enum AuthenticationIdentityEnum implements IBaseEnum<Integer> {

    USERNAME(1, "用户名密码登录"),
    MOBILE(2, "手机号登录"),
    SOCIAL(3, "第三方登录"),
    WECHAT_APPLET(4, "微信小程序"),
    DOUYIN_APPLET(5, "抖音小程序"),
    ;

    private final Integer value;
    private final String label;

}
