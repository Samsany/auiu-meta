package com.auiucloud.gateway.config;

import cn.hutool.core.util.ArrayUtil;
import com.auiucloud.core.cloud.props.MetaApiProperties;
import com.auiucloud.core.common.constant.Oauth2Constant;
import com.auiucloud.gateway.auth.AuthorizationManager;
import com.auiucloud.gateway.handler.RestAuthenticationEntryPoint;
import com.auiucloud.gateway.handler.RestfulAccessDeniedHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.oauth2.server.resource.authentication.ReactiveJwtAuthenticationConverterAdapter;
import org.springframework.security.web.server.SecurityWebFilterChain;
import reactor.core.publisher.Mono;

/**
 * 资源服务器配置
 *
 * @author dries
 * @date 2022/2/9
 */
@Configuration
@EnableWebFluxSecurity
@RequiredArgsConstructor
public class MeatResourceServerConfig {

    private final MetaApiProperties metaApiProperties;
    private final RestAuthenticationEntryPoint authenticationEntryPoint;
    private final RestfulAccessDeniedHandler accessDeniedHandler;
    private final AuthorizationManager authorizationManager;

    @Value("${spring.security.oauth2.resourceserver.jwt.jwk-set-uri}")
    private String jwkSetUri;

    @Bean
    JwtDecoder jwtDecoder() {
        return NimbusJwtDecoder.withJwkSetUri(this.jwkSetUri).build();
    }

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        http.oauth2ResourceServer(resourceServer -> resourceServer
                .jwt().jwtAuthenticationConverter(jwtAuthenticationTokenConverter()
                ));
        // 自定义处理JWT请求头过期或签名错误的结果
        http.oauth2ResourceServer(resourceServer -> resourceServer
                .authenticationEntryPoint(authenticationEntryPoint)
        );

        http.authorizeExchange(authorizeExchangeSpec -> authorizeExchangeSpec
                // 白名单配置
                .pathMatchers(ArrayUtil.toArray(metaApiProperties.getIgnoreUrl(), String.class)).permitAll()
                // 鉴权管理器配置
                .anyExchange().access(authorizationManager)
                .and().exceptionHandling()
                .accessDeniedHandler(accessDeniedHandler)
                .authenticationEntryPoint(authenticationEntryPoint)
                .and().csrf().disable()
        );

        return http.build();
    }

    private Converter<Jwt, ? extends Mono<? extends AbstractAuthenticationToken>> jwtAuthenticationTokenConverter() {

        var jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        jwtGrantedAuthoritiesConverter.setAuthorityPrefix(Oauth2Constant.ROLE_PREFIX);
        jwtGrantedAuthoritiesConverter.setAuthoritiesClaimName(Oauth2Constant.AUTHORITY_CLAIM_NAME);

        var jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter);

        return new ReactiveJwtAuthenticationConverterAdapter(jwtAuthenticationConverter);
    }
}
