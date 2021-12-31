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
     * 日志配置
     */
    public final String PROVIDER_LOG_SET = PROVIDER + "/log/set";
}
