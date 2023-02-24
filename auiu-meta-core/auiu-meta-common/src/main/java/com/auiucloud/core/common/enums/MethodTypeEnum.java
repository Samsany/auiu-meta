package com.auiucloud.core.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author dries
 * @date 2021/12/22
 */
@Getter
@AllArgsConstructor
public enum MethodTypeEnum {

    /**
     * 方法类型
     * GET
     * PUT
     * POST
     * DELETE
     * OPTIONS
     */
    GET(false),
    PUT(true),
    POST(true),
    DELETE(false),
    HEAD(false),
    OPTIONS(false);

    private final boolean hasContent;

}
