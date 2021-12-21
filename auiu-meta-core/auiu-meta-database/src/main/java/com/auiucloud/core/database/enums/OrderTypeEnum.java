package com.auiucloud.core.database.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 排序类型
 *
 * @author dries
 * @date 2021/12/21
 */
@Getter
@AllArgsConstructor
public enum OrderTypeEnum {

    /**
     * 排序规则
     * ASC 正序
     * DESC 倒序
     */
    ASC("asc"),
    DESC("desc");

    private final String value;

}
