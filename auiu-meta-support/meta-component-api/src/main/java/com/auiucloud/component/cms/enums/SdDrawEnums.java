package com.auiucloud.component.cms.enums;

import com.auiucloud.core.common.enums.IBaseEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author dries
 **/
public class SdDrawEnums {

    @Getter
    @AllArgsConstructor
    public enum QueueChangeType implements IBaseEnum<Integer> {

        INCREASE(0, "增加"),
        DECREASE(1, "减少"),
        ;

        private final Integer value;
        private final String label;
    }

}
