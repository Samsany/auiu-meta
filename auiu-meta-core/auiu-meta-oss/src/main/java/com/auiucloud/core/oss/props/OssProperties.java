package com.auiucloud.core.oss.props;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * AWS配置信息
 *
 * @author dries
 **/
@Data
public class OssProperties implements Serializable {
    @Serial
    private static final long serialVersionUID = 890060751082683761L;

    /**
     * 服务提供商
     * 阿里云 腾讯云 minIO等
     */
    private String provider;

    /**
     * 对象存储服务的URL
     */
    private String endpoint;

    /**
     * 自定义域名
     */
    private String customDomain;

    /**
     * true path-style nginx 反向代理和S3默认支持 pathStyle {http://endpoint/bucketname} false
     * supports virtual-hosted-style 阿里云等需要配置为 virtual-hosted-style
     * 模式{http://bucketname.endpoint}
     */
    private Boolean pathStyleAccess = true;

    /**
     * 应用ID
     */
    private String appId;

    /**
     * 区域
     */
    private String region;

    /**
     * Access key
     */
    private String accessKey;

    /**
     * Secret key
     */
    private String secretKey;

    /**
     * 默认的存储桶名称
     */
    private String bucketName = "meta";
}
