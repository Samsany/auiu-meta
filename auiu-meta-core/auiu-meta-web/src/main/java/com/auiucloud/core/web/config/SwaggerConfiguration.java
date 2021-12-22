package com.auiucloud.core.web.config;

import com.auiucloud.core.common.constant.MateConstant;
import com.auiucloud.core.common.utils.YamlPropertyLoaderFactory;
import com.auiucloud.core.web.props.MateSwaggerProperties;
import com.github.xiaoymin.knife4j.spring.extension.OpenApiExtensionResolver;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import springfox.bean.validators.configuration.BeanValidatorPluginsConfiguration;
import springfox.documentation.PathProvider;
import springfox.documentation.annotations.ApiIgnore;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.paths.DefaultPathProvider;
import springfox.documentation.spring.web.plugins.ApiSelectorBuilder;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebMvc;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author dries
 * @date 2021/12/22
 */
@Configuration
@EnableSwagger2WebMvc
@AllArgsConstructor
@Import(BeanValidatorPluginsConfiguration.class)
@EnableConfigurationProperties(MateSwaggerProperties.class)
@PropertySource(factory = YamlPropertyLoaderFactory.class, value = "classpath:swagger.yml")
public class SwaggerConfiguration {

    private final MateSwaggerProperties swaggerProperties;
    private final OpenApiExtensionResolver openApiExtensionResolver;
    /**
     * Swagger忽略的参数类型
     */
    private final Class[] ignoredParameterTypes = new Class[]{
            ServletRequest.class,
            ServletResponse.class,
            HttpServletRequest.class,
            HttpServletResponse.class,
            HttpSession.class,
            ApiIgnore.class,
            Principal.class,
            Map.class
    };

    @Bean
    public PathProvider pathProvider() {
        return new DefaultPathProvider() {
            @Override
            public String getOperationPath(String operationPath) {
                return super.getOperationPath(operationPath);
            }
        };
    }

    @Bean(value = "defaultApi3")
    public Docket createRestApi() {
        ApiSelectorBuilder apiSelectorBuilder = new Docket(DocumentationType.SWAGGER_2)
                // 用来创建该API的基本信息，展示在文档的页面中（自定义展示的信息）
                .apiInfo(groupApiInfo())
                // 设置哪些接口暴露给Swagger展示
                .select();
        if (swaggerProperties.getBasePackage() == null) {
            // 扫描所有有注解的api，用这种方式更灵活
            apiSelectorBuilder.apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class));
        } else {
            // 扫描指定的包
            apiSelectorBuilder.apis(RequestHandlerSelectors.basePackage(swaggerProperties.getBasePackage()));
        }
        return apiSelectorBuilder.paths(PathSelectors.any())
                .build()
                .enable(swaggerProperties.isEnable())
                .securitySchemes(securitySchemes())
                .securityContexts(securityContexts())
                .pathProvider(pathProvider())
                .ignoredParameterTypes(ignoredParameterTypes)
                .pathMapping("/").extensions(openApiExtensionResolver.buildSettingExtensions());
    }

    private ApiInfo groupApiInfo() {
        return new ApiInfoBuilder()
                .title(swaggerProperties.getTitle())
                .description(swaggerProperties.getDescription())
                .license(swaggerProperties.getLicense())
                .termsOfServiceUrl(swaggerProperties.getTermsOfServiceUrl())
                .contact(new Contact(
                        swaggerProperties.getContactName(),
                        swaggerProperties.getContactUrl(),
                        swaggerProperties.getContactEmail())
                )
                .version(MateConstant.AUIU_APP_VERSION)
                .build();
    }


    private List<SecurityScheme> securitySchemes() {
        // 设置请求头信息
        List<SecurityScheme> result = new ArrayList<>();
        ApiKey apiKey = new ApiKey("Authorization", "Authorization", "header");
        // apiKeyList.add(new ApiKey("Mate-Auth", "Mate-Auth", "header"));
        result.add(apiKey);
        return result;
    }

    /**
     * swagger2 认证的安全上下文
     */
    private List<SecurityContext> securityContexts() {
        List<SecurityContext> securityContexts = new ArrayList<>();
        securityContexts.add(
                SecurityContext.builder()
                        .securityReferences(defaultAuth())
                        .forPaths(PathSelectors.regex("^(?!auth).*$"))
                        .build());
        return securityContexts;
    }

    List<SecurityReference> defaultAuth() {
        AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;
        List<SecurityReference> securityReferences = new ArrayList<>();
        securityReferences.add(new SecurityReference("Authorization", authorizationScopes));
        return securityReferences;
    }

}
