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
    public final String AUIU_MATE_CLOUD_GATEWAY = "auiu-mate-gateway";

    /**
     * 系统服务
     */
    public final String AUIU_MATE_CLOUD_SYSTEM = "auiu-mate-system";

    /**
     * 认证服务
     */
    public final String AUIU_MATE_CLOUD_UAA = "auiu-mate-uaa";

    /**
     * 消息生产者
     */
    public final String AUIU_MATE_CLOUD_LOG_PRODUCER = "auiu-meta-log-producer";

}
