package com.auiucloud.admin.modules.system.enums;

import com.auiucloud.core.common.enums.IBaseEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author dries
 **/
@Getter
@AllArgsConstructor
public enum MenuOpenTypeEnum implements IBaseEnum<Integer> {

    /**
     * 无
     */
    NONE(0, "无"),
    /**
     * 组件
     */
    COMPONENT(1, "组件"),
    /**
     * 内链
     */
    IFRAME(2, "内链"),
    /**
     * 外链
     */
    LINK(3, "外链");

    private final Integer value;
    private final String label;
}
