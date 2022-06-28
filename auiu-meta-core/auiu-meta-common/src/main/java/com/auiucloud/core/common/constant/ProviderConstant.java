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
     * 根据userId查询用户角色信息
     */
    public final String PROVIDER_USER_ROLE_LIST = PROVIDER + "/role/list";

    /**
     * 根据roles查询用户权限信息
     */
    public final String PROVIDER_ROLE_PERMISSION_LIST = PROVIDER + "/role-permission/list";

    /**
     * 日志配置
     */
    public final String PROVIDER_LOG_SET = PROVIDER + "/log/set";
}
