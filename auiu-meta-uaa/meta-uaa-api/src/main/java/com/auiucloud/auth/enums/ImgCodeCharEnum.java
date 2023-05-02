package com.auiucloud.auth.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

/**
 * @author dries
 **/
@Getter
@AllArgsConstructor
public enum ImgCodeCharEnum {

    /**
     * 数字和字母混合
     */
    TYPE_DEFAULT,
    /**
     * 数字和字母混合
     */
    TYPE_ONLY_NUMBER,
    /**
     * 数字和字母混合
     */
    TYPE_ONLY_CHAR,
    /**
     * 数字和字母混合
     */
    TYPE_ONLY_UPPER,
    /**
     * 数字和字母混合
     */
    TYPE_ONLY_LOWER,
    /**
     * 数字和字母混合
     */
    TYPE_NUM_AND_UPPER

}
