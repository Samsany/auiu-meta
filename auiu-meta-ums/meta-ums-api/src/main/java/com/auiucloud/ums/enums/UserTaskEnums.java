package com.auiucloud.ums.enums;

import com.auiucloud.core.common.enums.IBaseEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @author dries
 **/
public class UserTaskEnums {

    /**
     * 任务类型
     */
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public enum UserTaskTypeEnum implements IBaseEnum<Integer> {

        NEW_USER_TASK(0, "新手任务"),
        DAY_TASK(1, "日常任务"),
        ACTIVITY_TASK(2, "活动任务"),
        ;

        private Integer value;
        private String label;
    }

    /**
     * 任务标识
     */
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public enum UserTaskTagEnum implements IBaseEnum<String> {

        WATCH_MOTIVATIONAL_VIDEO("100001", "观看激励视频"),
        ;

        private String value;
        private String label;
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public enum UserTaskCycleEnum implements IBaseEnum<String> {

        FOREVER("#", "不限次"),
        YEAR("Y", "年"),
        MONTH("M", "月"),
        WEEK("W", "周"),
        DAY("D", "日"),
        ;

        private String value;
        private String label;
    }

}
