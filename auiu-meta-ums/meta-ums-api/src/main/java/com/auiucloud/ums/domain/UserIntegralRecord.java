package com.auiucloud.ums.domain;

import com.auiucloud.core.database.model.BaseEntity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * 用户积分记录表
 * @TableName ums_user_integral_record
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
@TableName(value ="ums_user_integral_record")
public class UserIntegralRecord extends BaseEntity {


    @Serial
    private static final long serialVersionUID = -2334033704849986109L;

    /**
     * 用户ID
     */
    private Long uId;

    /**
     * 关联id(order,sign,system)
     */
    private Long linkId;

    /**
     * 关联类型(order)
     */
    private String linkType;

    /**
     * 类型(1-增加 2-扣减)
     */
    private Integer type;

    /**
     * 标题
     */
    private String title;

    /**
     * 积分
     */
    private Integer integral;

    /**
     * 剩余积分
     */
    private Integer balance;

    /**
     * 状态(1-预创建 2-冻结期 3-完成 4-失效)
     */
    private Integer status;

    /**
     * 冻结期时间(天)
     */
    private Integer frozenTime;

    /**
     * 解冻时间
     */
    private LocalDateTime thawTime;

}
