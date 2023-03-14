package com.auiucloud.auth.service.impl;

import cn.hutool.core.lang.Assert;
import com.auiucloud.core.common.constant.Oauth2Constant;
import com.auiucloud.core.common.enums.AuthenticationIdentityEnum;
import com.auiucloud.core.common.enums.IBaseEnum;
import com.auiucloud.core.common.utils.SecurityUtil;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.AuthenticationUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Map;

/**
 * 刷新token再次认证 UserDetailsService
 *
 * @author dries
 **/
public class PreAuthenticatedUserDetailsService<T extends Authentication> implements AuthenticationUserDetailsService<T>, InitializingBean {

    /**
     * 客户端ID和用户服务 UserDetailService 的映射
     */
    private Map<String, UserDetailsService> userDetailsServiceMap;

    public PreAuthenticatedUserDetailsService(Map<String, UserDetailsService> userDetailsServiceMap) {
        Assert.notNull(userDetailsServiceMap, "userDetailsService cannot be null.");
        this.userDetailsServiceMap = userDetailsServiceMap;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(userDetailsServiceMap, "userDetailsService cannot be null.");
    }

    /**
     * 重写PreAuthenticatedAuthenticationProvider 的 preAuthenticatedUserDetailsService 属性，可根据客户端和认证方式选择用户服务 UserDetailService 获取用户信息 UserDetail
     *
     * @param authentication
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserDetails(T authentication) throws UsernameNotFoundException {
        String clientId = SecurityUtil.getOAuth2ClientId();

        // 获取认证身份标识，默认是用户名:username
        AuthenticationIdentityEnum authenticationIdentityEnum = IBaseEnum.getEnumByValue(SecurityUtil.getAuthenticationIdentity(), AuthenticationIdentityEnum.class);
        UserDetailsService userDetailsService = userDetailsServiceMap.get(clientId);

        if (clientId.equals(Oauth2Constant.META_CLIENT_ADMIN_ID)) {
            // 管理系统的用户体系是系统用户，认证方式通过用户名 username 认证
            return switch (authenticationIdentityEnum) {
                default -> userDetailsService.loadUserByUsername(authentication.getName());
            };
        } else {
            return userDetailsService.loadUserByUsername(authentication.getName());
        }

        //       else if (clientId.equals(Oauth2Constant.APP_CLIENT_ID)) {
//            // 移动端的用户体系是会员，认证方式是通过手机号 mobile 认证
//            MemberUserDetailsServiceImpl memberUserDetailsService = (MemberUserDetailsServiceImpl) userDetailsService;
//            switch (authenticationIdentityEnum) {
//                case MOBILE:
//                    return memberUserDetailsService.loadUserByMobile(authentication.getName());
//                default:
//                    return memberUserDetailsService.loadUserByUsername(authentication.getName());
//            }
//        } else if (clientId.equals(SecurityConstants.WEAPP_CLIENT_ID)) {
//            // 小程序的用户体系是会员，认证方式是通过微信三方标识 openid 认证
//            MemberUserDetailsServiceImpl memberUserDetailsService = (MemberUserDetailsServiceImpl) userDetailsService;
//            switch (authenticationIdentityEnum) {
//                case OPENID:
//                    return memberUserDetailsService.loadUserByOpenId(authentication.getName());
//                default:
//                    return memberUserDetailsService.loadUserByUsername(authentication.getName());
//            }
//        } else {
//            return userDetailsService.loadUserByUsername(authentication.getName());
//        }
    }
}
