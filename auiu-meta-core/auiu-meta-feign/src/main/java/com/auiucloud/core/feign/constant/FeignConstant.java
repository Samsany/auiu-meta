package com.auiucloud.core.feign.constant;

import lombok.experimental.UtilityClass;

/**
 * OpenFeign常量类
 *
 * @author dries
 * @date 2021/12/24
 */
@UtilityClass
public class FeignConstant {

    /**
     * 网关
     */
    public final String AUIU_META_CLOUD_GATEWAY = "auiu-meta-gateway";
    /**
     * 认证服务
     */
    public final String AUIU_META_CLOUD_AUTH = "auiu-meta-auth";

    /**
     * 系统服务
     */
    public final String META_CLOUD_ADMIN = "meta-admin";

    /**
     * 消息生产者
     */
    public final String META_CLOUD_LOG_PRODUCER = "meta-log-producer";

}
