package com.auiucloud.component.sd.enums;

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

    @Getter
    @AllArgsConstructor
    public enum DrawType implements IBaseEnum<Integer> {

        TXT2IMG(0, "文生图"),
        ;

        private final Integer value;
        private final String label;
    }

    @Getter
    @AllArgsConstructor
    public enum DrawStatus implements IBaseEnum<Integer> {

        VIOLATIONS(-1, "违规"),
        WAITING(0, "等待中"),
        IN_PROGRESS(1, "进行中"),
        SUCCESS(2, "已完成"),
        FAIL(3, "生成失败"),
        ;

        private final Integer value;
        private final String label;
    }

}
