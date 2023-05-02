package com.auiucloud.auth.config;

import com.auiucloud.auth.extension.douyin.DouyinAuthenticationSecurityConfig;
import com.auiucloud.auth.extension.sms.SmsCodeAuthenticationSecurityConfig;
import com.auiucloud.auth.extension.social.SocialAuthenticationSecurityConfig;
import com.auiucloud.auth.extension.wechat.WechatAuthenticationSecurityConfig;
import com.auiucloud.auth.service.SmsCodeSenderService;
import com.auiucloud.auth.service.ValidateCodeService;
import com.auiucloud.auth.service.impl.DefaultSmsCodeSenderImpl;
import com.auiucloud.auth.service.impl.RedisValidateCodeServiceImpl;
import com.auiucloud.core.security.handle.MetaAuthenticationFailureHandler;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
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
public class MetaSecurityConfig {

    //    private final UserDetailsService userDetailsService;
    private final SmsCodeAuthenticationSecurityConfig smsCodeAuthenticationSecurityConfig;
    private final SocialAuthenticationSecurityConfig socialAuthenticationSecurityConfig;
    private final DouyinAuthenticationSecurityConfig douyinAuthenticationSecurityConfig;
    private final WechatAuthenticationSecurityConfig wechatAuthenticationSecurityConfig;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .logout(AbstractHttpConfigurer::disable)
                .sessionManagement(sessionManagement -> sessionManagement
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests()
                // 任何请求
                .anyRequest()
                // 全部放行 由网关处理授权
                .permitAll();

        http.apply(socialAuthenticationSecurityConfig);
        http.apply(smsCodeAuthenticationSecurityConfig);
        http.apply(wechatAuthenticationSecurityConfig);
        http.apply(douyinAuthenticationSecurityConfig);
        return http.build();
    }

    /**
     * 默认密码处理器
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    /**
     * 解决 无法直接注入 AuthenticationManager
     *
     * @return AuthenticationManager
     * @throws Exception 异常
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration auth) throws Exception {
        return auth.getAuthenticationManager();
    }

    @Bean
    public AuthenticationFailureHandler mateAuthenticationFailureHandler() {
        return new MetaAuthenticationFailureHandler();
    }

    @Bean
    @ConditionalOnMissingBean(SmsCodeSenderService.class)
    public SmsCodeSenderService smsCodeSenderService() {
        return new DefaultSmsCodeSenderImpl();
    }

    @Bean
    @ConditionalOnMissingBean(ValidateCodeService.class)
    public ValidateCodeService validateCodeService() {
        return new RedisValidateCodeServiceImpl();
    }

}
