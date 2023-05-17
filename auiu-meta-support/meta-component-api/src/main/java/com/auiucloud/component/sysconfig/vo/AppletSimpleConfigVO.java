package com.auiucloud.component.sysconfig.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author dries
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppletSimpleConfigVO implements Serializable {
    @Serial
    private static final long serialVersionUID = -7397042124027923059L;

    /**
     * appID
     */
    private String appId;

    /**
     * 小程序名称
     */
    private String appletName;

    /**
     * 是否开启激励视频 默认true
     */
    private Boolean isEnableVideoAd;

    /**
     * 激励视频广告ID
     */
    private String rewardedVideoAd;

    /**
     * 是否开启插屏广告 默认false
     */
    private Boolean isEnableInterstitialAd;

    /**
     * 插屏广告ID
     */
    private String interstitialAd;

    /**
     * 是否启用付费会员 默认 false
     */
    private Boolean isEnablePaidMember;

    /**
     * 积分充值功能启用 默认 false
     */
    private Boolean isEnablePointRecharge;

    /**
     * 积分充值注意事项
     */
    private String pointRechargeDesc;

    /**
     * 用户存储上限
     */
    private Integer userStorageLimit;

    /**
     * 付费用户存储上限
     */
    private Integer memberStorageLimit;

    /**
     * 提现方式
     */
    private String userExtractType;

}
