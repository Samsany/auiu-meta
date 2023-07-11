package com.auiucloud.core.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author dries
 **/
public class WsMessageEnums {


    @Getter
    @AllArgsConstructor
    public enum TypeEnum implements IBaseEnum<String> {


        ERROR("error", "error"),
        SD_START_WORK("sd-start-work", "开始工作..."),
        SD_PROGRESS("sd-progress", "绘画进度条"),
        SD_TXT2IMG("sd-txt2img", "SD文生图"),
        SD_TXT2IMG_QUEUE("sd-txt2img-queue", "SD文生图队列"),
        ;

        private final String value;
        private final String label;
    }

    @Getter
    @AllArgsConstructor
    public enum SendTypeEnum implements IBaseEnum<Integer> {

        /**
         * 发送类型:
         *
         * 0-发送给所有人
         * 1-指定用户发送
         * 2-指定部分用户发送
         * 3-排除部分用户发送
         */
        ALL(0, "发送给所有人"),
        USER(1, "指定用户发送"),
        IN_INCLUDE_USER(2, "指定部分用户发送"),
        EX_INCLUDE_USER(3, "排除部分用户发送"),
        ;

        private final Integer value;
        private final String label;
    }

}
