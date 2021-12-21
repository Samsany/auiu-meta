package com.auiucloud.core.cloud.props;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

/**
 * 链路追踪配置
 *
 * @author dries
 * @date 2021/12/20
 */
@Data
@RefreshScope
@ConfigurationProperties("auiu-cloud.request")
public class MetaRequestProperties {
    /**
     * 是否开启日志链路追踪
     */
    private boolean trace;

    /**
     * 是否启用获取IP地址
     */
    private boolean ip;

    /**
     * 是否启用增强模式
     */
    private boolean enhance;
}
