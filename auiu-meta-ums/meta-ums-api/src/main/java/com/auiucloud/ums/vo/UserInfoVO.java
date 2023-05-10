package com.auiucloud.ums.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author dries
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoVO implements Serializable {

    @Serial
    private static final long serialVersionUID = -4368943137148761820L;

    private Long id;

    private String account;

    /**
     * openId
     */
    private String openId;

    private String nickname;

    private String avatar;

    private String bgImg;

    /**
     * 是否为付费会员
     */
    private Boolean isPaidMember;

    /**
     * 付费会员过期时间
     */
    private LocalDateTime memberExpiredTime;

    /**
     * 性别(0-女性 1-男性 2-未知)
     */
    private Integer gender;

    /**
     * 手机号
     */
    private String mobile;

    /**
     * 邀请码
     */
    private String invitationCode;

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

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Boolean isAttention;

    // 关注数
    @Builder.Default
    private Long attentionNum = 0L;

    // 粉丝数
    @Builder.Default
    private Long followerNum = 0L;

    // 获赞数
    @Builder.Default
    private Long receivedLikeNum = 0L;

    // 作品数
    @Builder.Default
    private Long galleryNum = 0L;

    private String remark;

    private LocalDateTime createTime;
}
