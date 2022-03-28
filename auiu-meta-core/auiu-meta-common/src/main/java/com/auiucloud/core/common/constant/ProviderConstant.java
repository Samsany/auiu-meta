package com.auiucloud.core.common.constant;

import lombok.experimental.UtilityClass;

/**
 * @author dries
 * @date 2021/12/30
 */
@UtilityClass
public class ProviderConstant {

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
