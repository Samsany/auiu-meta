package com.auiucloud.admin.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author dries
 * @createDate 2022-06-08 17-00
 */
@Getter
@AllArgsConstructor
public enum MenuTypeEnum {

    /**
     * 目录
     */
    DIR(0, "目录"),
    /**
     * 菜单
     */
    MENU(1, "菜单"),
    /**
     * 按钮
     */
    BUTTON(2, "按钮");

    private final Integer code;

    private final String message;

}
