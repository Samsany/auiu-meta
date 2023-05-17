package com.auiucloud.ums.domain;

import com.auiucloud.core.database.model.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.Serial;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 用户佣金记录表
 *
 * @TableName ums_user_brokerage_record
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
@TableName(value = "ums_user_brokerage_record")
public class UserBrokerageRecord extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 7010883817387089917L;
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
     * 金额
     */
    private BigDecimal price;

    /**
     * 余额
     */
    private BigDecimal balance;

    /**
     * 状态(1-订单创建 2-冻结期 3-完成 4-失效)
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
