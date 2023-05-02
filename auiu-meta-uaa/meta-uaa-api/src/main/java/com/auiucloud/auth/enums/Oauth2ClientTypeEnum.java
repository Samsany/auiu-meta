package com.auiucloud.auth.enums;

import com.auiucloud.core.common.enums.IBaseEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author dries
 **/
@Getter
@AllArgsConstructor
public enum Oauth2ClientTypeEnum implements IBaseEnum<Integer> {

    ADMIN("管理端", 0),
    MEMBER("会员端", 1),
    ;

    private final String label;
    private final Integer value;

}
