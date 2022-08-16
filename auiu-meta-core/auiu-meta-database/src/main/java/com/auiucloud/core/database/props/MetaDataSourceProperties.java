package com.auiucloud.core.database.props;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

/**
 * @author dries
 * @createDate 2022-08-16 11-40
 */
@Data
@RefreshScope
@ConfigurationProperties(prefix = "meta.datasource")
public class MetaDataSourceProperties {

    /**
     * 地址
     */
    private String url;

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 驱动
     */
    private String driverClassName;

    /**
     * 加密公钥
     */
    private String publicKey;

}
