package com.auiucloud.component.sysconfig.enums;

import com.auiucloud.core.common.enums.IBaseEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author dries
 **/
public class SysConfigConstants {

    /**
     * appID
     */
    public static final String APP_ID = "app_id";
    /**
     * appSecret
     */
    public static final String APP_SECRET = "app_secret";
    /**
     * 小程序名称
     */
    public static final String APPLET_NAME = "applet_name";


    // #########     小程序配置参数     #############
    /**
     * 是否开启AI绘画
     */
    public static final String IS_ENABLE_AI_DRAW = "is_enable_ai_draw";
    /**
     * 是否开启激励视频广告
     */
    public static final String IS_ENABLE_VIDEO_AD = "is_enable_video_ad";
    /**
     * 激励视频广告ID
     */
    public static final String REWARDED_VIDEO_AD = "rewarded_video_ad";
    /**
     * 是否开启插屏广告
     */
    public static final String IS_ENABLE_INTERSTITIAL_AD = "is_enable_interstitial_ad";
    /**
     * 插屏广告ID
     */
    public static final String INTERSTITIAL_AD = "interstitial_ad";
    // #########     用户配置参数     #############
    public static final String USER_DEFAULT_AVATAR = "user_default_avatar";
    /**
     * 用户默认头像
     */
    public static final String USER_DEFAULT_BG_IMAGES = "user_default_bg_images";
    /**
     * 是否开启付费会员
     */
    public static final String IS_ENABLE_PAID_MEMBER = "is_enable_paid_member";
    /**
     * 是否开启积分充值功能
     */
    public static final String IS_ENABLE_POINT_RECHARGE = "is_enable_point_recharge";
    /**
     * 积分充值注意事项
     */
    public static final String POINT_RECHARGE_DESC = "point_recharge_desc";
    /**
     * 用户存储上限
     */
    public static final String USER_STORAGE_LIMIT = "user_storage_limit";
    /**
     * 付费会员存储上限
     */
    public static final String MEMBER_STORAGE_LIMIT = "member_storage_limit";
    /**
     * 激励广告兑换积分比例
     */
    public static final String AD_REDEEM_POINT = "ad_redeem_point";
    /**
     * 积分抵用比例
     */
    public static final String POINT_REDEMPTION_RATIO = "point_redemption_ratio";
    /**
     * 下载作品消耗默认积分
     */
    public static final String DEFAULT_DOWNLOAD_INTEGRAL = "default_download_integral";

    /**
     * 返佣配置
     */
    public static final String IS_ENABLE_SELF_PURCHASED_REBATE = "is_enable_self_purchased_rebate"; // 启用自购返佣
    public static final String IS_ENABLE_BUY_PAID_MEMBER_REBATE = "is_enable_buy_paid_member_rebate"; // 启用购买付费会员返佣
    public static final String IS_ENABLE_PROMOTE_USER_REBATE = "is_enable_promote_user_rebate"; // 启用推广用户返佣
    public static final String IS_ENABLE_DOWNLOAD_WORK_REBATE = "is_enable_download_work_rebate"; // 启用下载作品返佣
    public static final String PROMOTE_USER_AMOUNT = "promote_user_amount"; // 推广用户返佣金额
    public static final String DOWNLOAD_WORK_AMOUNT_RATIO = "download_work_amount_ratio"; // 下载作品返佣金额比例
    public static final String FIRST_REBATE_RATIO = "first_rebate_ratio"; // 一级返佣比例
    public static final String SECOND_REBATE_RATIO = "second_rebate_ratio"; // 二级返佣比例
    public static final String FREEZE_TIME = "freeze_time"; // 佣金冻结时间
    /**
     * 提现配置
     */
    public static final String USER_EXTRACT_TYPE = "user_extract_type"; // 用户提现方式
    public static final String USER_EXTRACT_MIN_PRICE = "user_extract_min_price"; // 用户提现最低金额
    public static final String MEMBER_EXTRACT_MIN_PRICE = "member_extract_min_price"; // 付费会员提现最低金额
    @Getter
    @AllArgsConstructor
    public enum SettingEnum implements IBaseEnum<String> {

        BASE_SETTING("基础信息配置", "system"),
        OSS_SETTING("OSS资源配置", "oss"),
        APP_SETTING("APP配置", "sys_app"),
        APPLET_SETTING("小程序配置", "sys_app_applets"),
        USER_SETTING("用户配置", "sys_user_config"),
        FENXIAO_SETTING("分销配置", "fenxiao_config"),
        ;

        private final String label;
        private final String value;
    }
    @Getter
    @AllArgsConstructor
    public enum SettingTypeEnum implements IBaseEnum<Integer> {

        SETTING("配置", 0),
        VALUE("值", 1),
        ;

        private final String label;
        private final Integer value;
    }
    /**
     * 0-隐私协议 1-用户协议 2-付费会员协议 3-积分协议 4-积分充值协议 5-注销协议
     */
    @Getter
    @AllArgsConstructor
    public enum ProtocolTypeEnum implements IBaseEnum<Integer> {

        PRIVACY_AGREEMENT("隐私协议", 0),
        USER_AGREEMENT("用户协议", 1),
        PAID_MEMBERSHIP_AGREEMENT("付费会员协议", 2),
        POINT_AGREEMENT("积分协议", 3),
        POINT_RECHARGE_AGREEMENT("积分充值协议", 4),
        USER_WRITE_OFF_AGREEMENT("注销协议", 5),
        FAQ("常见问题", 6),
        ;

        private final String label;
        private final Integer value;
    }


}
