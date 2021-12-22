package com.auiucloud.core.redis.props;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author dries
 * @date 2021/12/22
 */
@Data
@ConfigurationProperties(prefix = "auiu-cloud.redis")
public class MetaRedisProps {

    /**
     * 是否开启
     */
    private boolean enable = true;

}
