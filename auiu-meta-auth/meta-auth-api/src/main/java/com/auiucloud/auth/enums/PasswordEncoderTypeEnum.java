package com.auiucloud.auth.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 密码编码类型枚举
 *
 * @author dries
 **/
@Getter
@AllArgsConstructor
public enum PasswordEncoderTypeEnum {

    BCRYPT("{bcrypt}","BCRYPT加密"),
    NOOP("{noop}","无加密明文");

    private final String prefix;
    private final String desc;


}
