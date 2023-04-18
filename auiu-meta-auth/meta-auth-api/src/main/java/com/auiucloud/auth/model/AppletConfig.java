package com.auiucloud.auth.model;

import lombok.Data;

/**
 * @author dries
 **/
@Data
public class AppletConfig {

    /**
     * 设置小程序的appid
     */
    private String appId;

    /**
     * 设置小程序的Secret
     */
    private String secret;

    /**
     * 设置小程序消息服务器配置的token
     */
    private String token;

    /**
     * 设置小程序消息服务器配置的EncodingAESKey
     */
    private String aesKey;

    /**
     * 消息格式，XML或者JSON
     */
    private String msgDataFormat;
}
