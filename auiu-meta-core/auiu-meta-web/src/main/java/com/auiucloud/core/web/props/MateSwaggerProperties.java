package com.auiucloud.core.web.props;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

import java.util.ArrayList;
import java.util.List;

/**
 * Swagger配置属性
 *
 * @author dries
 * @date 2021/12/22
 */
@Data
@RefreshScope
@ConfigurationProperties(prefix = "auiu-cloud.swagger")
public class MateSwaggerProperties {

    /*** 是否开启swagger */
    private boolean enabled = true;
    /*** API文档生成基础路径 */
    private String basePackage;
    /*** group */
    private String groupName = "default";
    /*** swagger会解析的url规则 */
    private List<String> basePath = new ArrayList<>();
    /*** 在basePath基础上需要排除的url规则 */
    private List<String> excludePath = new ArrayList<>();
    /*** 是否要启用登录认证 */
    private boolean enableSecurity = true;

    /*** 文档标题 */
    private String title = "swagger";
    /*** 文档描述 */
    private String description = "swagger document";
    /*** 文档版本 */
    private String version = "1.0";
    /*** 许可证 */
    private String license;
    /**
     * 许可证URL
     */
    private String licenseUrl;
    /*** 服务条款URL */
    private String termsOfServiceUrl = "https://www.auiucloud.com";
    /*** host信息 */
    private String host;
    /*** 文档联系人姓名 */
    private String contactName;
    /*** 文档联系人网址 */
    private String contactUrl;
    /*** 文档联系人邮箱 */
    private String contactEmail;

}
