package com.auiucloud.auth.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author dries
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppletAuthCallback implements Serializable {
    @Serial
    private static final long serialVersionUID = 8681137482123965183L;

    private String appId;
    private String openId;
    private String unionId;
    private String code;
    private String iv;
    private String encryptedData;
    private String source;
    private String rawUserInfo;

}
