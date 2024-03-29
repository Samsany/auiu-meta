package com.auiucloud.core.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @author dries
 * @createDate 2022-07-01 00-07
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum OrderTypeEnum {

    /**
     * 排序规则
     * ASC 正序
     * DESC 倒序
     */
    ASC("asc", "升序"),
    DESC("desc", "倒序");

    private String value;
    private String description;

}
