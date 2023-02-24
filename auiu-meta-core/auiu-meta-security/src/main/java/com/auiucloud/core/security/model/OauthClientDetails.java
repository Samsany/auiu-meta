package com.auiucloud.core.security.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * @author dries
 * @date 2022/3/1
 */
@Data
public class OauthClientDetails implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 客户端ID
     */
    @Schema(description = "客户端ID")
    private String clientId;
    /**
     * 客户端名称
     */
    @Schema(description = "客户端名称")
    private String clientName;
    /**
     * 资源ID
     */
    @Schema(description = "资源ID")
    private String resourceIds;
    /**
     * 客户端密匙
     */
    @Schema(description = "客户端密匙")
    private String clientSecret;
    /**
     * 权限范围
     */
    @Schema(description = "权限范围")
    private String scope;
    /**
     * 授权类型
     */
    @Schema(description = "授权类型")
    private String authorizedGrantTypes;
    /**
     * 重定向路径
     */
    @Schema(description = "重定向路径")
    private String webServerRedirectUri;
    /**
     * 用户权限
     */
    @Schema(description = "用户权限")
    private String authorities;
    /**
     * 令牌过期秒数
     */
    @Schema(description = "令牌过期秒数")
    private Integer accessTokenValidity;
    /**
     * 刷新令牌过期秒数
     */
    @Schema(description = "刷新令牌过期秒数")
    private Integer refreshTokenValidity;
    /**
     * 附加的信息
     */
    @Schema(description = "附加的信息")
    private String additionalInformation;
    /**
     * 自动授权
     */
    @Schema(description = "自动授权")
    private String autoApprove;
}
