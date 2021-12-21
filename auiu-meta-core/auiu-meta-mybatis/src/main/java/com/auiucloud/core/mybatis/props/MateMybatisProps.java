package com.auiucloud.core.mybatis.props;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

/**
 * @author dries
 * @date 2021/12/21
 */
@Data
@Component
@RefreshScope
@ConfigurationProperties("auiu-cloud.mybatis")
public class MateMybatisProps {
    /**
     * 是否打印可执行 sql
     */
    private boolean sql = true;
}
