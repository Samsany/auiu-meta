package com.auiucloud.core.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public enum QueryModeEnum {

    PAGE("page", "分页查询"),

    LIST("list", "列表查询"),

    TREE("tree", "树形列表"),

    CASCADE("cascade", "级联列表"),
    ;

    private String code;
    private String description;

    public static QueryModeEnum getQueryModeByCode(String code) {
        for (QueryModeEnum value : values()) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        // 默认分页查询
        return PAGE;
    }
}
