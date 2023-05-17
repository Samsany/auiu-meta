package com.auiucloud.ums.enums;

import com.auiucloud.core.common.enums.IBaseEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @author dries
 **/
public class UserBrokerageEnums {

    /**
     * 佣金来源
     */
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public enum SourceEnum implements IBaseEnum<String> {

        DOWNLOAD_WORKS("gallery", "下载作品"),
        INVITE_FRIENDS("invite-user", "邀请好友"),
        ;

        private String value;
        private String label;
    }

    /**
     * 佣金变动类型
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

}
