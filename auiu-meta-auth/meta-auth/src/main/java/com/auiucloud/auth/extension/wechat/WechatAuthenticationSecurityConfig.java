package com.auiucloud.auth.extension.wechat;

import com.auiucloud.core.security.service.MetaUserDetailService;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author dries
 **/
@Component
public class WechatAuthenticationSecurityConfig extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {

    @Resource
    private MetaUserDetailService userDetailsService;

    @Override
    public void configure(HttpSecurity http) {
        // 获取社交登录提供者
        WechatAuthenticationProvider wechatAuthenticationProvider = new WechatAuthenticationProvider(userDetailsService);
        // 将社交登录校验器注册到 HttpSecurity
        http.authenticationProvider(wechatAuthenticationProvider);
    }

}
