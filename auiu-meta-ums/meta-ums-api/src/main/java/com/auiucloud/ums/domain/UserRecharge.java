package com.auiucloud.ums.domain;

import com.auiucloud.core.database.model.BaseEntity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * 用户充值表
 * @TableName ums_user_recharge
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
@TableName(value ="ums_user_recharge")
public class UserRecharge extends BaseEntity {

    @Serial
    private static final long serialVersionUID = -7810048748654164822L;

    /**
     * 用户ID
     */
    private Long uId;

    /**
     * 订单号
     */
    private String orderSn;

    /**
     * 充值到账金额
     */
    private BigDecimal price;

    /**
     * 实际充值金额
     */
    private BigDecimal otPrice;

    /**
     * 赠送金额
     */
    private BigDecimal givePrice;

    /**
     * 充值类型(bank-银行卡 alipay-支付宝 wechat-微信)
     */
    private String rechargeType;

}
