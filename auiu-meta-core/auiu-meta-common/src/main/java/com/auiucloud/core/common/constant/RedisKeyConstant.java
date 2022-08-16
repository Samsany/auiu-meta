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

    /**
     * ---- Redis缓存权限规则key 缓存相关 ----
     */
    public static final String ALL_PERMISSION = "*:*:*";
    public static final String PERMISSION_ROLES_KEY = "auth:permission:roles";
    public static final String URL_PERM_ROLES_KEY = "system:permission:url_perm_roles";
    public static final String BTN_PERM_ROLES_KEY = "system:permission:btn_perm_roles";
    public static final String TOKEN_BLACKLIST_PREFIX = "auth:token:blacklist:";

}
