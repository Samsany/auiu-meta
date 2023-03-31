package com.auiucloud.auth.config.properties;

import com.auiucloud.auth.model.AppletConfig;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author dries
 **/
@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "meta.applets.douyin")
public class DouyinAppletsProperties {

    private List<AppletConfig> configs;

}
