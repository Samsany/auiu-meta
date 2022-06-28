package com.auiucloud.core.rule.constant;

/**
 * @author dries
 * @date 2021/12/27
 */
public class RuleConstant {

    public static final String LOCALHOST = "localhost";
    public static final String LOCALHOST_IP = "127.0.0.1";

    public static final String ALL = "all";
    public static final String BLACKLIST_OPEN = "0";
    public static final String BLACKLIST_CLOSE = "1";

    private static final String BLACKLIST_CACHE_KEY_PREFIX = "meta:rule:blacklist:";

    public static String getBlackListCacheKey(String ip) {
        if (LOCALHOST.equalsIgnoreCase(ip)) {
            ip = LOCALHOST_IP;
        }
        return String.format("%s%s", BLACKLIST_CACHE_KEY_PREFIX, ip);
    }

    public static String getBlackListCacheKey() {
        return String.format("%s" + ALL, BLACKLIST_CACHE_KEY_PREFIX);
    }

}
