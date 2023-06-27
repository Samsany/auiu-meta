package com.auiucloud.component.sysconfig.domain;

import com.auiucloud.core.common.constant.CommonConstant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * 用户配置
 *
 * @author dries
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserConfigProperties implements Serializable {

    @Serial
    private static final long serialVersionUID = 3242829739007736241L;

    /**
     * 用户默认头像
     */
    private String userDefaultAvatar;

    /**
     * 用户默认背景图
     */
    private String userDefaultBgImages;

    /**
     * 是否启用付费会员 默认 false
     */
    @Builder.Default
    private Boolean isEnablePaidMember = Boolean.FALSE;

    /**
     * 积分充值功能启用 默认 false
     */
    private Boolean isEnablePointRecharge = Boolean.FALSE;

    /**
     * 积分充值注意事项
     */
    private String pointRechargeDesc;

    /**
     * 用户存储上限
     */
    @Builder.Default
    private Integer userStorageLimit = 1500;

    /**
     * 付费用户存储上限
     */
    @Builder.Default
    private Integer memberStorageLimit = 3000;

    /**
     * 激励广告兑换积分比例 默认为1
     */
    @Builder.Default
    private Integer adRedeemPoint = CommonConstant.STATUS_DISABLE_VALUE;

    /**
     * 自购返佣
     */
    @Builder.Default
    private Boolean isEnableSelfPurchasedRebate = Boolean.FALSE;

    /**
     * 是否启用购买付费会员返佣
     */
    @Builder.Default
    private Boolean isEnableBuyPaidMemberRebate = Boolean.FALSE;

    /**
     * 是否启用推广用户返佣
     */
    @Builder.Default
    private Boolean isEnablePromoteUserRebate = Boolean.FALSE;

    /**
     * 是否启用下载作品返佣
     */
    @Builder.Default
    private Boolean isEnableDownloadWorkRebate = Boolean.FALSE;

    /**
     * 推广用户返佣金额 默认0
     */
    @Builder.Default
    private String promoteUserAmount = "0";

    /**
     * 下载作品返佣金额 默认0
     */
    @Builder.Default
    private Integer downloadWorkAmountRatio = 0;

    /**
     * 一级返佣比例 默认20%
     */
    @Builder.Default
    private Integer firstRebateRatio = 20;

    /**
     * 二级返佣比例 默认0
     */
    @Builder.Default
    private Integer secondRebateRatio = 0;

    /**
     * 佣金冻结时间 默认7天
     */
    @Builder.Default
    private Integer freezeTime = 7;

    /**
     * 积分抵用比例 默认20%
     */
    @Builder.Default
    private Integer pointRedemptionRatio = 20;

    /**
     * 下载作品默认消耗积分 默认1
     */
    @Builder.Default
    private Integer defaultDownloadIntegral = 1;

    /**
     * 提现方式
     */
    @Builder.Default
    private String userExtractType = "微信,支付宝,银行卡";

    /**
     * 用户提现最低金额
     */
    @Builder.Default
    private Integer userExtractMinPrice = 100;

    /**
     * 付费会员提现最低金额(-1代表不限制)
     */
    @Builder.Default
    private Integer memberExtractMinPrice = -1;

}
