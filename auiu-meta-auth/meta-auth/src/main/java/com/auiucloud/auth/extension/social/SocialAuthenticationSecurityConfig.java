package com.auiucloud.auth.extension.social;

import com.auiucloud.core.security.service.MetaUserDetailService;
import com.xkcoding.justauth.AuthRequestFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author dries
 **/
@Component
public class SocialAuthenticationSecurityConfig extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {

    @Resource
    private MetaUserDetailService userDetailsService;

    @Resource
    private AuthRequestFactory authRequestFactory;

    @Override
    public void configure(HttpSecurity http) {

        // 过滤器
        SocialAuthenticationFilter socialAuthenticationFilter = new SocialAuthenticationFilter();
        socialAuthenticationFilter.setAuthenticationManager(http.getSharedObject(AuthenticationManager.class));
        socialAuthenticationFilter.setAuthRequestFactory(authRequestFactory);

        // 获取社交登录提供者
        SocialAuthenticationProvider socialAuthenticationProvider = new SocialAuthenticationProvider(userDetailsService);

        // 将社交登录校验器注册到 HttpSecurity， 并将社交登录过滤器添加在 UsernamePasswordAuthenticationFilter 之前
        http.authenticationProvider(socialAuthenticationProvider)
                .addFilterBefore(socialAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
    }

}
