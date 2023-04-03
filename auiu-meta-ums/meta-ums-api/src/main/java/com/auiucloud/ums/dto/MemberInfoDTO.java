package com.auiucloud.ums.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author dries
 **/
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "会员登录信息传输对象")
public class MemberInfoDTO {

    /**
     * 账号ID
     */
    private Long id;

    /**
     * 账号
     */
    private String account;

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
     * 密码
     */
    private String password;

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
    private Integer accountNonExpired;

    /**
     * 账户是否锁定(0-锁定 1-未锁定)
     */
    private Integer accountNonLocked;

    /**
     * 证书(密码)是否过期(0-过期 1-未过期)
     */
    private Integer credentialsNonExpired;

    /**
     * openId
     */
    private String openId;
    private String unionId;

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

}
