package com.auiucloud.component.sysconfig.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * 小程序基础配置
 *
 * @author dries
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppletConfigProperties implements Serializable {


    @Serial
    private static final long serialVersionUID = 287825523409764218L;

    /**
     * appID
     */
    private String appId;

    /**
     * appSecret
     */
    private String appSecret;

    /**
     * 小程序名称
     */
    private String appletName;

    /**
     * 是否开启Ai绘画功能 默认FALSE
     */
    @Builder.Default
    private Boolean isEnableAiDraw = Boolean.FALSE;

    /**
     * 是否开启激励视频 默认FALSE
     */
    @Builder.Default
    private Boolean isEnableVideoAd = Boolean.FALSE;

    /**
     * 激励视频广告ID
     */
    private String rewardedVideoAd;

    /**
     * 是否开启插屏广告 默认FALSE
     */
    @Builder.Default
    private Boolean isEnableInterstitialAd = Boolean.FALSE;

    /**
     * 插屏广告ID
     */
    private String interstitialAd;


}
