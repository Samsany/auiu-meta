package com.auiucloud.core.web.props;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author dries
 **/
@Data
@ConfigurationProperties(prefix = "meta.httpclient")
public class HttpClientProperties {

    /**
     * 设置同时间正在使用的最大连接数，默认值是20。
     */
    private Integer maxTotal = 5000;

    /**
     * 设置一个 host(ip或域名):port 同时间正在使用的最大连接数，默认值是2。
     */
    private Integer maxPerRoute = 5000;

    /**
     * 返回数据的超时时间
     */
    private Integer socketTimeout = -1;
    /**
     * 连接服务的超时时间
     */
    private Integer connectTimeout = 5000;
    /**
     * 从连接池获取连接的超时时间
     */
    private Integer connectRequestTimeout = 1000;

}
