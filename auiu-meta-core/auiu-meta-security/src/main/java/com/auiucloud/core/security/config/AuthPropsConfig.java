package com.auiucloud.core.security.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author dries
 * @date 2022/3/1
 */
@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "auiu-cloud.security")
public class AuthPropsConfig {

    private boolean singleLogin;

}
