package com.auiucloud.auth.model;

import lombok.Data;

/**
 * @author dries
 **/
@Data
public class AppletAccessTokenInfo {

    private String access_token;
    private Integer expires_in;

}
