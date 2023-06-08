package com.auiucloud.core.redis.props;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

/**
 * @author dries
 * @date 2021/12/22
 */
@Data
@ConfigurationProperties(MetaRedisProperties.PREFIX)
public class MetaRedisProperties {

    /**
     * 前缀
     */
    public static final String PREFIX = "meta.redis";

    /**
     * 是否开启
     */
    private boolean enabled = true;

}
