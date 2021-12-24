package com.auiucloud.core.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @author dries
 * @date 2021/12/22
 */
@Getter
@NoArgsConstructor
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

    private String code;

    private String message;

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
