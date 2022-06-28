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
    public final String META_CLOUD_GATEWAY = "meta-gateway";

    /**
     * 系统服务
     */
    public final String META_CLOUD_ADMIN = "meta-admin";

    /**
     * 认证服务
     */
    public final String META_CLOUD_UAA = "meta-uaa";

    /**
     * 消息生产者
     */
    public final String META_CLOUD_LOG_PRODUCER = "meta-log-producer";

}
