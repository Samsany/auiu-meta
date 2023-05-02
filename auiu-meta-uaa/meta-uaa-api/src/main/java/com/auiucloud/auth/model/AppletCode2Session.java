package com.auiucloud.auth.model;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author dries
 **/
@Data
public class AppletCode2Session implements Serializable {
    @Serial
    private static final long serialVersionUID = 260331162799657696L;

    private String session_key;

    private String openid;

    private String anonymous_openid;

    private String unionid;

    private String dopenid;
}
