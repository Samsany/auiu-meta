package com.auiucloud.core.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author dries
 * @date 2021/12/22
 */
@Getter
@AllArgsConstructor
public enum StatusEnum {

    /**
     * 启用
     */
    ENABLE("enable", "启用"),

    /**
     * 禁用
     */
    DISABLE("disable", "禁用");

    private final String code;

    private final String message;

    /**
     * 根据code获取枚举
     */
    public static StatusEnum codeToEnum(String code) {
        if (null != code) {
            for (StatusEnum e : StatusEnum.values()) {
                if (e.getCode().equals(code)) {
                    return e;
                }
            }
        }
        return null;
    }

}
