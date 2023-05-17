package com.auiucloud.ums.domain;

import com.auiucloud.core.database.model.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.Serial;
import java.time.LocalDateTime;

/**
 * 付费会员等级记录表
 *
 * @TableName ums_user_level_record
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
@TableName(value = "ums_user_level_record")
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
    private LocalDateTime expiredTime;

}
