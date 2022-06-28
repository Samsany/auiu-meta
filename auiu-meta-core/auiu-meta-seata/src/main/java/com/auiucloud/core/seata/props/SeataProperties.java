package com.auiucloud.core.seata.props;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author dries
 * @date 2021/12/22
 */
@Data
@ConfigurationProperties(prefix = "meta.seata")
public class SeataProperties {

    private String applicationId;

    private String txServiceGroup;

}
