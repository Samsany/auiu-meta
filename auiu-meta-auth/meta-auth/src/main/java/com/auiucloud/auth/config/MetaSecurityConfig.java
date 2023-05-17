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
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
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

    private final UserDetailsService userDetailsService;
    private final SmsCodeAuthenticationSecurityConfig smsCodeAuthenticationSecurityConfig;
    private final SocialAuthenticationSecurityConfig socialAuthenticationSecurityConfig;
    private final DouyinAuthenticationSecurityConfig douyinAuthenticationSecurityConfig;
    private final WechatAuthenticationSecurityConfig wechatAuthenticationSecurityConfig;

    @SneakyThrows
    @Override
    protected void configure(HttpSecurity http) {
        ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry config = http
                .apply(socialAuthenticationSecurityConfig)
                .and()
                .apply(smsCodeAuthenticationSecurityConfig)
                .and()
                .apply(wechatAuthenticationSecurityConfig)
                .and()
                .apply(douyinAuthenticationSecurityConfig)
                .and()
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .logout(AbstractHttpConfigurer::disable)
                .sessionManagement(sessionManagement -> sessionManagement
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeRequests();

        config
                // 任何请求
                .anyRequest()
                // 全部放行 由网关处理授权
                .permitAll();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                // 配置 AuthenticationManager 使用 userService
                .userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder());
    }

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
