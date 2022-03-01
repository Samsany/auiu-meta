package com.auiucloud.core.security.config;

import com.auiucloud.core.security.handle.RestAuthenticationEntryPoint;
import com.auiucloud.core.security.handle.RestfulAccessDeniedHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;

/**
 * 资源服务配置
 *
 * @author dries
 * @date 2021/12/22
 */
@Order(9)
@EnableResourceServer
@RequiredArgsConstructor
@EnableAutoConfiguration(exclude = UserDetailsServiceAutoConfiguration.class)
public class MetaResourceServerConfig extends ResourceServerConfigurerAdapter {

    private final IgnoreUrlPropsConfig ignoreUrlPropsConfig;
//    private final RedisConnectionFactory redisConnectionFactory;
//
//    /**
//     * 配置token存储到redis中
//     */
//    @Bean
//    public RedisTokenStore redisTokenStore() {
//        return new RedisTokenStore(redisConnectionFactory);
//    }

    @Override
    public void configure(HttpSecurity http) throws Exception {

        http
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .logout(AbstractHttpConfigurer::disable)
                .sessionManagement(sessionManagement -> sessionManagement
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry config = http.authorizeRequests();
        ignoreUrlPropsConfig.getUrls().forEach(url -> config.antMatchers(url).permitAll());
        ignoreUrlPropsConfig.getIgnoreSecurity().forEach(url -> config.antMatchers(url).permitAll());
        config
                // 任何请求
                .anyRequest()
                // 都需要身份认证
                .authenticated();
    }

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        resources.authenticationEntryPoint(new RestAuthenticationEntryPoint())
                .accessDeniedHandler(new RestfulAccessDeniedHandler());
    }
}
