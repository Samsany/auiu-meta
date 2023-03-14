package com.auiucloud.core.common.constant;

/**
 * @author dries
 * @date 2022/2/8
 */
public class RedisKeyConstant {

    /**
     * 管理端路径前缀
     */
    public static final String ADMIN_URL_PATTERN = "/meta-admin";

    public static final String DEVICE_HEADER_KEY = "key";
    public static final String DEVICE_HEADER_CODE = "code";
    /**
     * 验证码
     */
    public static final String CAPTCHA_KEY = "meta.captcha.";
    /**
     * 短信
     */
    public static final String SMS_CODE_KEY = "meta.sms.code.";
    /**
     * 设计登录
     */
    public static final String SOCIAL_CACHE_PREFIX = "SOCIAL::STATE::";

    /**
     * ---- Redis缓存权限规则key 缓存相关 ----
     */
    public static final String PERMISSION_ROLES_KEY = "auth:permission:roles";
    public static final String URL_PERM_ROLES_KEY = "system:permission:url_perm_roles";
    public static final String BTN_PERM_ROLES_KEY = "system:permission:btn_perm_roles";
    public static final String TOKEN_BLACKLIST_PREFIX = "auth:token:blacklist:";

    /**
     * 权限标识前缀
     */
    public static final String META_PERMISSION_PREFIX = "meta.permission.";

    /**
     * 客户端缓存Key
     */
    public static String cacheClientKey(String clientId) {
        return "meta.oauth_client." + clientId;
    }

}
