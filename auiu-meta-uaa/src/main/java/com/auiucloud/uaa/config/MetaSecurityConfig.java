package com.auiucloud.uaa.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.autoconfigure.security.servlet.EndpointRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsPasswordService;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 安全配置中心
 * @author dries
 * @date 2022/2/27
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class MetaSecurityConfig extends WebSecurityConfigurerAdapter {

    @Value("${spring.security.oauth2.resourceserver.jwt.jwk-set-uri}")
    private String jwkSetUri;

    private final PasswordEncoder passwordEncoder;
    private final UserDetailsService userDetailsService;
    private final UserDetailsPasswordService userDetailsPasswordService;
//    private final RestAuthenticationEntryPoint authenticationEntryPoint;
//    private final RestfulAccessDeniedHandler accessDeniedHandler;

    /**
     * Spring Security OAuth2 支持 grant_type=password
     * 必须要配置 AuthenticationManager
     *
     * @return AuthenticationManager
     * @throws Exception 异常
     */
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                //.apply(socialAuthenticationSecurityConfig)
                //.and()
                //.apply(smsCodeAuthenticationSecurityConfig)
                //.and()
                //.apply(validateCodeSecurityConfig)
                //.and()
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .logout(AbstractHttpConfigurer::disable)
                .sessionManagement(sessionManagement -> sessionManagement
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeRequests(authorizeRequests -> authorizeRequests
                        .requestMatchers(EndpointRequest.toAnyEndpoint()).permitAll()
                        .anyRequest().permitAll()
                )
//                .exceptionHandling(exceptionHandling -> exceptionHandling
//                        .authenticationEntryPoint(authenticationEntryPoint)
//                        .accessDeniedHandler(accessDeniedHandler)
//                )
                .oauth2ResourceServer(resourceServer -> resourceServer
                        .jwt().jwtAuthenticationConverter(jwtAuthenticationTokenConverter())
                );
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                // 配置 AuthenticationManager 使用 userService
                .userDetailsService(userDetailsService)
                // 配置 AuthenticationManager 使用 userService
                .passwordEncoder(passwordEncoder)
                // 配置密码自动升级服务
                .userDetailsPasswordManager(userDetailsPasswordService);
    }

    @Bean
    JwtDecoder jwtDecoder() {
        return NimbusJwtDecoder.withJwkSetUri(this.jwkSetUri).build();
    }

    private Converter<Jwt, AbstractAuthenticationToken> jwtAuthenticationTokenConverter() {
        return jwt -> {
            List<String> userAuthorities = jwt.getClaimAsStringList("authorities");
            List<String> scopes = jwt.getClaimAsStringList("scope");
            List<GrantedAuthority> combinedAuthorities = Stream.concat(
                            userAuthorities.stream(),
                            scopes.stream().map(scope -> "SCOPE_" + scope))
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());
            String username = jwt.getClaimAsString("user_name");
            return new UsernamePasswordAuthenticationToken(username, null, combinedAuthorities);
        };
    }

}
