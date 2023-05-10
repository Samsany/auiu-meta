package com.auiucloud.core.douyin.props;

import com.auiucloud.core.douyin.model.AppletConfig;
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
@ConfigurationProperties(prefix = "meta.applets")
public class AppletsProperties {

    private List<AppletConfig> configs;

}
