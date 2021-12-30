package com.auiucloud.core.common.constant;

import lombok.experimental.UtilityClass;

/**
 * 常量
 * @author dries
 * @date 2021/12/20
 */
@UtilityClass
public class MetaConstant {

    /**
     * 应用版本号
     */
    public final String AUIU_APP_VERSION = "1.0.0";

    /**
     * 微服务之间传递的唯一标识
     */
    public final String META_TRACE_ID = "meta-trace-id";

    /**
     * 日志链路追踪id日志标志
     */
    public final String LOG_TRACE_ID = "traceId";

    public static final String CONFIG_DATA_ID_DYNAMIC_ROUTES = "auiu-meta-dynamic-routes.yaml";
    public static final String CONFIG_GROUP = "DEFAULT_GROUP";
    public static final long CONFIG_TIMEOUT_MS = 5000;
}
