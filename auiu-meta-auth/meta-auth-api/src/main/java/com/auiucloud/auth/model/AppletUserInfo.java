package com.auiucloud.auth.model;

import com.auiucloud.core.common.utils.GsonUtil;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author dries
 **/
@Data
@With
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppletUserInfo implements Serializable {

    @Serial
    private static final long serialVersionUID = -9194711224130442100L;

    private Long userId;

    private String openId;

    private String unionId;

    private String account;

    private String nickName;

    private String avatarUrl;

    private int gender;

    private String city;

    private String province;

    private String country;

    private String language;

    public static AppletUserInfo fromJson(String json) {
        return GsonUtil.fromJson(json, AppletUserInfo.class);
    }

}
