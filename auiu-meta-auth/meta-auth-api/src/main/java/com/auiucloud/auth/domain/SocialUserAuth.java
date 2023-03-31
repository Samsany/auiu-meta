package com.auiucloud.auth.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serial;
import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 第三方用户 & 系统用户关联表
 * @TableName social_user_auth
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName(value ="social_user_auth")
public class SocialUserAuth implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 序号
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 系统用户ID
     */
    private Long userId;

    /**
     * 第三方用户ID
     */
    private Long socialUserId;
}
