package com.auiucloud.auth.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author dries
 **/
@Data
@TableName(value = "sys_oauth_client")
public class MetaClientDetails implements Serializable {

    @Serial
    private static final long serialVersionUID = -1137148846605617055L;

    /**
     * 编号主键标识
     */
    @TableId
    private Long id;

    /**
     * 客户端类型
     */
    private Integer clientType;

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
     * authorization_code,password,refresh_token,client_credentials,sms,captcha,social
     */
    private String authorizedGrantTypes;

    /**
     * 重定向路径
     * 可选，客户端的重定向URI,当grant_type为authorization_code或implicit时,此字段是需要的
     */
    private String webServerRedirectUri;

    /**
     * 用户权限
     * 可选，指定客户端所拥有的Spring Security的权限值
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
     * 预留字段，格式必须是json
     */
    private String additionalInformation;

    /**
     * 自动授权
     * 该字段适用于grant_type="authorization_code"的情况下，用户是否自动approve操作
     */
    private String autoApprove;
    /**
     * 客户端状态(0-正常 1-禁用)
     */
    private Integer status;

}
