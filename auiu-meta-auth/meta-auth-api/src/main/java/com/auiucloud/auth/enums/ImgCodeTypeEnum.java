package com.auiucloud.auth.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

/**
 * @author dries
 **/
@Getter
@AllArgsConstructor
public enum ImgCodeTypeEnum {

    /**
     * png类型
     */
    SPECC,
    /**
     * gif类型
     */
    GIF,
    /**
     * 中文类型
     */
    CHINESE,
    /**
     * 中文gif类型
     */
    CHINESE_GIF,
    /**
     * 算数类型
     */
    ARITHMETIC

}
