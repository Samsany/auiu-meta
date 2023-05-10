package com.auiucloud.core.rocketmq.config;

import com.auiucloud.core.common.utils.YamlPropertyLoaderFactory;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.PropertySource;

/**
 * @author dries
 **/
@AutoConfiguration
@PropertySource(factory = YamlPropertyLoaderFactory.class, value = "classpath:rocketmq.yml")
public class RocketMQConfiguration {

}
