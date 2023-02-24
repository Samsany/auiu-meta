package com.auiucloud.gateway.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.AbstractSwaggerUiConfigProperties;
import org.springdoc.core.SwaggerUiConfigProperties;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionLocator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.util.*;

/**
 * spring-doc 配置
 *
 * @author dries
 */
@Slf4j
@Configuration(proxyBeanMethods = false)
@RequiredArgsConstructor
public class SwaggerConfig {

    private final RouteDefinitionLocator routeDefinitionLocator;
    private final SwaggerUiConfigProperties swaggerUiConfigProperties;
    private static final String API_URI = "/v3/api-docs";

    @Bean
    public OpenAPI customOpenAPI() {
        Contact contact = new Contact();
        contact.setName("dries");

        return new OpenAPI().info(
                new Info()
                        .title("AUIU-META-CLOUD")
                        .description("微服务开发框架")
                        .contact(contact)
                        .version("1.0.0_beta")
                        .termsOfService("https://auiucloud.com")
                        .license(
                                new License()
                                        .name("开发版")
                                        .url("https://auiucloud.com"))
        );
    }

    @PostConstruct
    public void autoInitSwaggerUrls() {
        List<RouteDefinition> definitions = routeDefinitionLocator.getRouteDefinitions().collectList().block();

        Optional.ofNullable(definitions).orElse(Collections.emptyList()).forEach(routeDefinition -> {
            AbstractSwaggerUiConfigProperties.SwaggerUrl swaggerUrl = new AbstractSwaggerUiConfigProperties.SwaggerUrl(
                    routeDefinition.getId().replace("ReactiveCompositeDiscoveryClient_", "").toLowerCase(),
                    routeDefinition.getUri().toString().replace("lb://", "").toLowerCase() + API_URI,
                    routeDefinition.getUri().toString().replace("lb://", "").toLowerCase()
            );
            Set<AbstractSwaggerUiConfigProperties.SwaggerUrl> urls = swaggerUiConfigProperties.getUrls();
            if (urls == null) {
                urls = new LinkedHashSet<>();
                swaggerUiConfigProperties.setUrls(urls);
            }
            urls.add(swaggerUrl);
            log.error("{}", swaggerUiConfigProperties.getUrls());
        });
    }

}
