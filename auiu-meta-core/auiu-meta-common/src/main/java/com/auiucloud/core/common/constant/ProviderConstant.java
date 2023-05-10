package com.auiucloud.core.common.constant;

import lombok.experimental.UtilityClass;

/**
 * 远程调用常量
 *
 * @author dries
 * @date 2021/12/30
 */
@UtilityClass
public class ProviderConstant {

    /**
     * 请求超时时长
     */
    public static final long CONFIG_TIMEOUT_MS = 5000;

    /**
     * 模块公共前缀
     */
    public final String MODULE_PATH_PREFIX = "/meta";

    /**
     * 远程调用公共前缀
     */
    public final String PROVIDER = "/provider";
    /**
     * 根据username查询用户信息
     */
    public final String PROVIDER_USER_LIST = PROVIDER + "/user/list";
    /**
     * 根据username查询用户信息
     */
    public final String PROVIDER_USER_USERNAME = PROVIDER + "/user/username";
    /**
     * 根据手机号查询用户信息
     */
    public final String PROVIDER_USER_MOBILE = PROVIDER + "/user/mobile";
    /**
     * 根据openId + source查询用户信息
     */
    public final String PROVIDER_USER_OPENID = PROVIDER + "/user/openid";
    /**
     * 根据uuid + source查询用户信息
     */
    public final String PROVIDER_USER_SOCIAL = PROVIDER + "/user/social";

    /**
     * 日志配置
     */
    public final String PROVIDER_LOG_SET = PROVIDER + "/log/set";

    /**
     * 第三方用户注册
     */
    public final String PROVIDER_USER_REGISTER_SOCIAL = PROVIDER + "/user/register/social";

    /**
     * 抖音内容安全检测
     */
    public final String PROVIDER_DOUYIN_CHECK_TEXT = PROVIDER + "/douyin/check/text";
    /**
     * 抖音内容安全检测
     */
    public final String PROVIDER_DOUYIN_CHECK_TEXTS = PROVIDER + "/douyin/check/texts";
    /**
     * 抖音图片安全检测
     */
    public final String PROVIDER_DOUYIN_CHECK_IMAGE = PROVIDER + "/douyin/check/image";
    public final String PROVIDER_DOUYIN_CHECK_IMAGE_DATA = PROVIDER + "/douyin/check/image-data";

    // ---------------------------------------------------- META-COMPONENT -------------------------------------------------------------
    /**
     * 根据用户ID查询作品数量
     */
    public final String PROVIDER_USER_GALLERY_NUM = PROVIDER + "/user/gallery/count";
    /**
     * 根据用户ID查询作品收到的赞数量
     */
    public final String PROVIDER_USER_GALLERY_LIKE_NUM = PROVIDER + "/user/gallery-like/count";

}
