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
     * 认证服务
     */
    public final String META_CLOUD_AUTH = "meta-auth";
    /**
     * 系统服务
     */
    public final String META_CLOUD_ADMIN = "meta-admin";
    /**
     * 插件服务
     */
    public final String META_CLOUD_COMPONENT = "meta-component";
    /**
     * 会员服务
     */
    public final String META_CLOUD_MEMBER = "meta-ums";

    /**
     * 消息生产者
     */
    public final String META_CLOUD_LOG_PRODUCER = "meta-log-producer";

}
