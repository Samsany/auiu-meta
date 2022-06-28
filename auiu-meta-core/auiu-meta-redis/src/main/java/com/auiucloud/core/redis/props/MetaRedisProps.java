package com.auiucloud.core.redis.props;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author dries
 * @date 2021/12/22
 */
@Data
@ConfigurationProperties(MetaRedisProps.PREFIX)
public class MetaRedisProps {

    /**
     * 前缀
     */
    public static final String PREFIX = "mate.lettuce.redis";

    /**
     * 是否开启
     */
    private boolean enabled = true;

}
