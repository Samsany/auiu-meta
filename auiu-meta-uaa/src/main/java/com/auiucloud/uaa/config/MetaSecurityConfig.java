package com.auiucloud.uaa.config;

import com.auiucloud.core.security.handle.MetaAuthenticationFailureHandler;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

/**
 * 安全配置中心
 *
 * @author dries
 * @date 2022/2/27
 */

@Order(1)
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class MetaSecurityConfig extends WebSecurityConfigurerAdapter {

    // private final PasswordEncoder passwordEncoder;
    private final UserDetailsService userDetailsService;

    /**
     * 默认密码处理器
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    /**
     * Spring Security OAuth2 支持 grant_type=password
     * 必须要配置 AuthenticationManager
     *
     * @return AuthenticationManager
     */
    @Bean
    @Override
    @SneakyThrows
    public AuthenticationManager authenticationManagerBean() {
        return super.authenticationManagerBean();
    }

    @Bean
    public AuthenticationFailureHandler mateAuthenticationFailureHandler() {
        return new MetaAuthenticationFailureHandler();
    }

    @Override
    protected void configure(HttpSecurity http) {
//        http
//                .apply(socialAuthenticationSecurityConfig)
//                .and()
//                .apply(smsCodeAuthenticationSecurityConfig)
//                .and()
//                .apply(validateCodeSecurityConfig)
//                .and()
//                .formLogin(AbstractHttpConfigurer::disable)
//                .httpBasic(AbstractHttpConfigurer::disable)
//                .csrf(AbstractHttpConfigurer::disable)
//                .logout(AbstractHttpConfigurer::disable)
//                .sessionManagement(sessionManagement -> sessionManagement
//                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
//                .authorizeRequests(authorizeRequests -> authorizeRequests
//                        .requestMatchers(EndpointRequest.toAnyEndpoint()).permitAll()
//                        .anyRequest().permitAll()
//                )
//                .exceptionHandling(exceptionHandling -> exceptionHandling
//                        .authenticationEntryPoint(authenticationEntryPoint)
//                        .accessDeniedHandler(accessDeniedHandler)
//                )
//                .oauth2ResourceServer(resourceServer -> resourceServer
//                        .jwt().jwtAuthenticationConverter(jwtAuthenticationTokenConverter())
//                );
//        ;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                // 配置 AuthenticationManager 使用 userService
                .userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder());
    }

}
