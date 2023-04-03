package com.auiucloud.ums.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author dries
 **/
@Data
@With
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "会员登录信息VO")
public class MemberInfoVO {

    /**
     * 账号
     */
    private Long userId;

    /**
     * 账号
     */
    private String account;
    /**
     * 密码
     */
    private String password;

    /**
     * 真实姓名
     */
    private String realName;

    /**
     * 用户昵称
     */
    private String nickname;

    /**
     * 头像
     */
    private String avatar;

    /**
     * 手机号
     */
    private String mobile;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 性别(0-女性 1-男性 2-未知)
     */
    private Integer gender;

    /**
     * 出生日期
     */
    private Date birthday;

    /**
     * 是否启用(0-正常 1-禁用)
     */
    private Integer status;

    /**
     * 账户是否过期(0-过期 1-未过期)
     */
    private boolean accountNonExpired;

    /**
     * 账户是否锁定(0-锁定 1-未锁定)
     */
    private boolean accountNonLocked;

    /**
     * 证书(密码)是否过期(0-过期 1-未过期)
     */
    private boolean credentialsNonExpired;

    /**
     * openId
     */
    private String openId;

    /**
     * 会员积分
     */
    private Integer point;

    /**
     * 会员余额
     */
    private BigDecimal balance;

    /**
     * 国家
     */
    private String country;

    /**
     * 省
     */
    private String province;

    /**
     * 市
     */
    private String city;

    /**
     * 区
     */
    private String area;

    /**
     * 语言
     */
    private String language;

    /**
     * 最后登录时间
     */
    private Date loginDate;

    /**
     * 登录ip
     */
    private String loginIp;

    /**
     * 注册ip
     */
    private String registerIp;

    /**
     * 注册地址
     */
    private String registerAddress;

    /**
     * 注册方式
     */
    private String registerSource;

    /**
     * 登录类型　1：用户名密码登录　2：手机号登录　3：社交登录
     */
    @Schema(description = "登录类型")
    private String loginType;

}
