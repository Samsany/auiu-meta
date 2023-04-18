package com.auiucloud.ums.domain;

import com.auiucloud.core.database.model.BaseEntity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.SuperBuilder;

/**
 * 会员表
 * @TableName ums_member
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
@TableName(value ="ums_user")
@Schema(name = "Member对象", description = "会员表")
public class Member extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 6079480840276847321L;

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
    private LocalDate birthday;

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
     * 用户登录类型
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

}
