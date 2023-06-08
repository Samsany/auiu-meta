package com.auiucloud.ums.enums;

import com.auiucloud.core.common.enums.IBaseEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @author dries
 **/
public class UserPointEnums {

    /**
     * 积分来源
     */
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public enum SourceEnum implements IBaseEnum<Integer> {

        NEW_USER_TASK(0, "系统赠送"),
        ACTIVITY(1, "活动获取"),
        LOGIN(2, "登录"),
        SIGN(3, "签到"),
        COMMENT(4, "评论"),
        TASK(5, "任务赠送"),
        LUCKY_DRAW(5, "抽奖（福利兑换）"),
        ;

        private Integer value;
        private String label;
    }

    /**
     * 消耗方式
     */
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public enum ConsumptionEnum implements IBaseEnum<Integer> {

        DOWNLOAD_GALLERY(0, "下载作品"),
        AI_TEXT2IMG(0, "文生图"),
        ;

        private Integer value;
        private String label;
    }

    /**
     * 积分变动类型
     */
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public enum ChangeTypeEnum implements IBaseEnum<Integer> {

        INCREASE(0, "增加"),
        DECREASE(1, "减少"),
        ;

        private Integer value;
        private String label;
    }

    /**
     * 积分变动状态
     */
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public enum StatusEnum implements IBaseEnum<Integer> {

        INIT(0, "预变动"),
        FROZEN(1, "冻结期"),
        SUCCESS(2, "已完成"),
        INVALIDATED(3, "已失效"),
        ;

        private Integer value;
        private String label;
    }

}
