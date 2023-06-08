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

/**
 * 用户提现表
 *
 * @TableName ums_user_extract
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
@TableName(value = "ums_user_extract")
public class UserExtract extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 4538985473311170355L;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 名称
     */
    private String realName;

    /**
     * 提现类型(bank-银行卡 alipay-支付宝 wechat-微信)
     */
    private String extractType;

    /**
     * 银行卡号
     */
    private String bankCode;

    /**
     * 开户行
     */
    private String bankName;

    /**
     * 开户地址(支行)
     */
    private String bankAddress;

    /**
     * 支付宝账号
     */
    private String alipayCode;

    /**
     * 微信账号
     */
    private String wechatCode;

    /**
     * 提现金额
     */
    private BigDecimal extractPrice;

    /**
     * 提现状态(-1-未通过 0-审核中 1-已提现)
     */
    private Integer status;

    /**
     * 提现设置
     */
    private Integer cashConfigId;

    /**
     * 拒绝理由
     */
    private String refuseReason;


}
