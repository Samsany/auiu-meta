package com.auiucloud.core.database.config;

import com.auiucloud.core.common.utils.YamlPropertyLoaderFactory;
import com.auiucloud.core.database.props.MetaDataSourceProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * Druid配置
 *
 * @author dries
 * @date 2021/12/21
 */
@Configuration
@EnableConfigurationProperties(MetaDataSourceProperties.class)
@PropertySource(factory = YamlPropertyLoaderFactory.class, value = "classpath:database-druid.yml")
// 打开druid加密模式
//@PropertySource(value = "classpath:database-decrypt.properties")
public class DruidConfiguration {
}
