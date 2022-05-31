package com.auiucloud.core.security.model;

import lombok.Data;

/**
 * @author dries
 * @date 2022/3/1
 */
@Data
public class OauthClientDetails {
    private static final long serialVersionUID = 1L;

    /**
     * 客户端ID
     */
    private String clientId;
    /**
     * 客户端名称
     */
    private String clientName;
    /**
     * 资源ID
     */
    private String resourceIds;
    /**
     * 客户端密匙
     */
    private String clientSecret;
    /**
     * 权限范围
     */
    private String scope;
    /**
     * 授权类型
     */
    private String authorizedGrantTypes;
    /**
     * 重定向路径
     */
    private String webServerRedirectUri;
    /**
     * 用户权限
     */
    private String authorities;
    /**
     * 令牌过期秒数
     */
    private Integer accessTokenValidity;
    /**
     * 刷新令牌过期秒数
     */
    private Integer refreshTokenValidity;
    /**
     * 附加的信息
     */
    private String additionalInformation;
    /**
     * 自动授权
     */
    private String autoApprove;
}
