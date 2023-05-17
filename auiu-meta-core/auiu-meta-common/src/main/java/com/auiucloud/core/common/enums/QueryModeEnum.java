package com.auiucloud.core.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum QueryModeEnum implements IBaseEnum<String> {

    PAGE("page", "分页查询"),

    LIST("list", "列表查询"),

    TREE("tree", "树形列表"),

    CASCADE("cascade", "级联列表"),
    ;

    private final String value;
    private final String label;

    public static QueryModeEnum getQueryModeByCode(String code) {
        for (QueryModeEnum value : values()) {
            if (value.getValue().equals(code)) {
                return value;
            }
        }
        // 默认分页查询
        return PAGE;
    }
}
