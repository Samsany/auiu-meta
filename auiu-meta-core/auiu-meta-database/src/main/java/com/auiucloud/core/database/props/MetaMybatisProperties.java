package com.auiucloud.core.database.props;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

/**
 * @author dries
 * @date 2021/12/20
 */
@Data
@RefreshScope
@ConfigurationProperties(prefix = "meta.mybatis")
public class MetaMybatisProperties {

    /**
     * 是否打印可执行 sql
     */
    private boolean sql = true;

}
