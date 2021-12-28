package com.auiucloud.log.props;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author dries
 * @date 2021/12/27
 */
@Data
@ConfigurationProperties(prefix = "auiu-cloud.kafka")
public class LogProperties {

    /**
     * 是否启用
     */
    private boolean enable = false;

    /**
     * 记录日志类型
     */
    private LogType logType = LogType.DB;

}
