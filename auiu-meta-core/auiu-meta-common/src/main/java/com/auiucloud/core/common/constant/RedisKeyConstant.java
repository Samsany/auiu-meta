package com.auiucloud.core.common.constant;

/**
 * @author dries
 * @date 2022/2/8
 */
public class RedisKeyConstant {

    // redis根目录
    public static final String ROOT_DIR = "meta:";

    /**
     * 图形验证码
     */
    public static final String CAPTCHA_KEY = ROOT_DIR + "captcha:image:";
    /**
     * 短信验证码
     */
    public static final String SMS_CODE_KEY = ROOT_DIR + "captcha:sms:";
    /**
     * 此配置需与just-auth state保持一致
     */
    public static final String SOCIAL_CACHE_PREFIX = ROOT_DIR + "SOCIAL::STATE::";
    /**
     * 抖音小程序 access_token
     */
    public static String DOUYIN_APPLET_ACCESS_TOKEN = ROOT_DIR + "system-config:applets:douyin:access_token::";
    /**
     * OSS默认配置
     */
    public static String OSS_DEFAULT_CONFIG = ROOT_DIR + "system-config:oss";

    /**
     * ---- Redis缓存权限规则key 缓存相关 ----
     */
    public static final String TOKEN_BLACKLIST_PREFIX = ROOT_DIR + "auth-token:blacklist:";
    public static final String PERMISSION_ROLES_KEY = ROOT_DIR + "auth-perm:roles";
    public static final String URL_PERM_ROLES_KEY = ROOT_DIR + "auth-perm:url_perm_roles";
    public static final String BTN_PERM_ROLES_KEY = ROOT_DIR + "auth-perm:btn_perm_roles";
    public static final String META_PERMISSION_PREFIX = ROOT_DIR + "auth-perm:";

    /**
     * 客户端缓存Key
     */
    public static String cacheClientKey(String clientId) {
        return ROOT_DIR + "auth-client:" + clientId;
    }

}
