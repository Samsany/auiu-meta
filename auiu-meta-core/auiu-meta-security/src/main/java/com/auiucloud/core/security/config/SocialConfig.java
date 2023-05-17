package com.auiucloud.core.security.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author dries
 **/
@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "meta.social")
public class SocialConfig {
    private String url;

}
