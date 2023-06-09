package com.auiucloud.core.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author dries
 **/

public class FileEnums {

    @Getter
    @AllArgsConstructor
    public enum FileTypeEnum implements IBaseEnum<Integer> {

        PIC(1, "图片"),
        VIDEO(2, "视频"),
        OTHER(3, "其他"),
        ;

        private final Integer value;
        private final String label;
    }

}
