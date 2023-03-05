package com.auiucloud.core.common.enums;

import com.auiucloud.core.common.enums.IBaseEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author dries
 **/
@Getter
@AllArgsConstructor
public enum AuthenticationIdentityEnum implements IBaseEnum<String> {

    USERNAME("username", "用户名"),
    MOBILE("mobile", "手机号"),
    OPENID("openId", "开放式认证系统唯一身份标识");

    private final String value;
    private final String label;


}
