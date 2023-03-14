package com.auiucloud.auth.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serial;
import java.io.Serializable;
import lombok.Data;

/**
 * 第三方用户表
 * @TableName social_user
 */
@TableName(value ="social_user")
@Data
public class SocialUser implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 序号
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 第三方系统的唯一ID
     */
    private String uuid;

    /**
     * 第三方用户来源
     */
    private String source;

    /**
     * 用户的授权令牌
     */
    private String accessToken;

    /**
     * 第三方用户的授权令牌的有效期
     */
    private Integer expireIn;

    /**
     * 刷新令牌
     */
    private String refreshToken;

    /**
     * 第三方用户的 open id
     */
    private String openId;

    /**
     * 第三方用户的 ID
     */
    private String uid;

    /**
     * 个别平台的授权信息
     */
    private String accessCode;

    /**
     * 第三方用户的 union id
     */
    private String unionId;

    /**
     * 第三方用户授予的权限
     */
    private String scope;

    /**
     * 个别平台的授权信息
     */
    private String tokenType;

    /**
     * id token
     */
    private String idToken;

    /**
     * 小米平台用户的附带属性
     */
    private String macAlgorithm;

    /**
     * 小米平台用户的附带属性
     */
    private String macKey;

    /**
     * 用户的授权code
     */
    private String code;

    /**
     * Twitter平台用户的附带属性
     */
    private String oauthToken;

    /**
     * Twitter平台用户的附带属性
     */
    private String oauthTokenSecret;

}
