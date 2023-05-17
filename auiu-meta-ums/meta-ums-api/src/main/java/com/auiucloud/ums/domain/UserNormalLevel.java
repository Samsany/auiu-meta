package com.auiucloud.ums.domain;

import com.auiucloud.core.database.model.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.Serial;

/**
 * 普通会员等级表
 *
 * @TableName ums_user_normal_level
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
@TableName(value = "ums_user_normal_level")
@EqualsAndHashCode(callSuper = true)
public class UserNormalLevel extends BaseEntity {

    @Serial
    private static final long serialVersionUID = -7048085775081254028L;

    /**
     * 会员名称
     */
    private String name;

    /**
     * 升级经验
     */
    private Integer experience;

    /**
     * 状态(0-正常 1-隐藏)
     */
    private Integer status;

    /**
     * 会员等级
     */
    private Integer grade;

    /**
     * 享受折扣
     */
    private Integer discount;

    /**
     * 等级图标
     */
    private String icon;

    /**
     * 等级背景
     */
    private String bgImg;

    /**
     * 排序
     */
    private Integer sort;

}
