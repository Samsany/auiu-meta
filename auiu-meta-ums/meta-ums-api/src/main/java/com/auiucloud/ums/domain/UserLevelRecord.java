package com.auiucloud.ums.domain;

import com.auiucloud.core.database.model.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serial;
import java.util.Date;

import lombok.*;
import lombok.experimental.SuperBuilder;

/**
 * 付费会员等级记录表
 * @TableName ums_user_level
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
@TableName(value ="ums_user_level")
@EqualsAndHashCode(callSuper = true)
public class UserLevelRecord extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 1959452512569451958L;

    /**
     * 用户uid
     */
    private Long uid;

    /**
     * 等级vip
     */
    private Long levelId;

    /**
     * 会员等级
     */
    private Integer grade;

    /**
     * 0:禁止,1:正常
     */
    private Integer status;

    /**
     * 备注
     */
    private String mark;

    /**
     * 是否已通知
     */
    private Integer remind;

    /**
     * 享受折扣
     */
    private Integer discount;

    /**
     * 过期时间
     */
    private Date expiredTime;

}
