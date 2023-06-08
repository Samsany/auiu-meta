package com.auiucloud.core.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author dries
 **/
public class MessageEnums {


    @Getter
    @AllArgsConstructor
    public enum WsMessageTypeEnum implements IBaseEnum<String> {


        ERROR("error", "error"),
        SD_START_WORK("sd-start-work", "开始工作..."),
        SD_PROGRESS("sd-progress", "绘画进度条"),
        SD_TXT2IMG("sd-txt2img", "SD文生图"),
        SD_TXT2IMG_QUEUE("sd-txt2img-queue", "SD文生图队列"),
        ;

        private final String value;
        private final String label;
    }

}
