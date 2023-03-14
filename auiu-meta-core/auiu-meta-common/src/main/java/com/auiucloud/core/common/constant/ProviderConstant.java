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
    public final String PROVIDER_USER_USERNAME = PROVIDER + "/user/username";

    /**
     * 根据手机号查询用户信息
     */
    public final String PROVIDER_USER_MOBILE = PROVIDER + "/user/mobile";

    /**
     * 日志配置
     */
    public final String PROVIDER_LOG_SET = PROVIDER + "/log/set";
}
