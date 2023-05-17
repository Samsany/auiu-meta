package com.auiucloud.ums.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
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
     * UID
     */
    private Long userId;

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
     * 背景
     */
    private String bgImg;

    /**
     * 邀请码
     */
    private String invitationCode;

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
    @Builder.Default
    private boolean accountNonExpired = true;

    /**
     * 账户是否锁定(0-锁定 1-未锁定)
     */
    @Builder.Default
    private boolean accountNonLocked = true;

    /**
     * 证书(密码)是否过期(0-过期 1-未过期)
     */
    @Builder.Default
    private boolean credentialsNonExpired = true;

    /**
     * openId
     */
    private String openId;

    /**
     * 身份证号码
     */
    private String cardId;

    /**
     * 用户分组id
     */
    private String groupIds;

    /**
     * 用户标签id
     */
    private String tagIds;

    /**
     * 会员余额
     */
    private BigDecimal balance;

    /**
     * 佣金余额
     */
    private BigDecimal brokeragePrice;

    /**
     * 用户积分
     */
    private Integer integral;

    /**
     * 用户经验
     */
    private Integer experience;

    /**
     * 连续签到天数
     */
    private Integer signNum;

    /**
     * 用户等级
     */
    private Long levelId;

    /**
     * 推广员id
     */
    private Long spreadUid;

    /**
     * 推广员关联时间
     */
    private Date spreadTime;

    /**
     * 成为分销员时间
     */
    private Date promoterTime;

    /**
     * 是否为推广员
     */
    private Integer isPromoter;

    /**
     * 用户下单次数
     */
    private Integer payCount;

    /**
     * 下级人数
     */
    private Integer spreadCount;

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
     * 用户登录类型 1：用户名密码登录　2：手机号登录　3：社交登录
     */
    private String loginType;

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
     * 注册时间
     */
    private LocalDateTime createTime;
}
