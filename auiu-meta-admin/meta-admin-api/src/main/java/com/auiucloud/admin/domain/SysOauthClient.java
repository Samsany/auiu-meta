package com.auiucloud.admin.domain;

import com.auiucloud.core.database.model.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * 系统客户端表
 *
 * @author dries
 * @TableName sys_oauth_client
 * @createDate 2022-05-31 14:59:09
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
@TableName(value = "sys_oauth_client")
@ApiModel(value = "SysOauthClient对象", description = "系统客户端表")
public class SysOauthClient extends BaseEntity {

    private static final long serialVersionUID = 3028179941291254164L;

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

}
