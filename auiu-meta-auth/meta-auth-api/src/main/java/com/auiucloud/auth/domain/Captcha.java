package com.auiucloud.auth.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.With;
import lombok.experimental.SuperBuilder;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * @author dries
 **/
@Data
@With
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class Captcha implements Serializable {

    @Serial
    private static final long serialVersionUID = -726386871846706286L;

    private String uuid;
    /**
     * 验证码
     */
    private String code;
    /**
     * 过期时间
     */
    private Date expireTime;
    /**
     * 验证码内容
     */
    private String content;

    @JsonIgnore
    public boolean isExpired() {
        return new Date().after(expireTime);
    }


    @JsonIgnore
    public Date getExpiredTime(long expireIn) {
        return Date.from(new Date().toInstant().plusSeconds(expireIn));
    }

}
