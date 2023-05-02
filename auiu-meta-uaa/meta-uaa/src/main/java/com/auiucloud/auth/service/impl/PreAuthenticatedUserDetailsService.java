//package com.auiucloud.auth.service.impl;
//
//import cn.hutool.core.lang.Assert;
//import com.auiucloud.auth.extension.douyin.DouyinAuthenticationToken;
//import com.auiucloud.auth.extension.social.SocialAuthenticationToken;
//import com.auiucloud.core.common.constant.Oauth2Constant;
//import com.auiucloud.core.common.enums.AuthenticationIdentityEnum;
//import com.auiucloud.core.common.enums.IBaseEnum;
//import com.auiucloud.core.common.utils.SecurityUtil;
//import org.springframework.beans.factory.InitializingBean;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.userdetails.AuthenticationUserDetailsService;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//
//import java.util.Map;
//
///**
// * 刷新token再次认证 UserDetailsService
// *
// * @author dries
// **/
//public class PreAuthenticatedUserDetailsService<T extends Authentication> implements AuthenticationUserDetailsService<T>, InitializingBean {
//
//    /**
//     * 客户端ID和用户服务 UserDetailService 的映射
//     */
//    private final Map<String, UserDetailsService> userDetailsServiceMap;
//
//    public PreAuthenticatedUserDetailsService(Map<String, UserDetailsService> userDetailsServiceMap) {
//        Assert.notNull(userDetailsServiceMap, "userDetailsService cannot be null.");
//        this.userDetailsServiceMap = userDetailsServiceMap;
//    }
//
//    @Override
//    public void afterPropertiesSet() throws Exception {
//        Assert.notNull(userDetailsServiceMap, "userDetailsService cannot be null.");
//    }
//
//    /**
//     * 重写PreAuthenticatedAuthenticationProvider 的 preAuthenticatedUserDetailsService 属性，可根据客户端和认证方式选择用户服务 UserDetailService 获取用户信息 UserDetail
//     *
//     * @param authentication
//     * @return
//     * @throws UsernameNotFoundException
//     */
//    @Override
//    public UserDetails loadUserDetails(T authentication) throws UsernameNotFoundException {
//        String clientId = SecurityUtil.getOAuth2ClientId();
//
//        // 获取认证身份标识，默认是用户名:username
//        AuthenticationIdentityEnum authenticationIdentityEnum = IBaseEnum.getEnumByValue(SecurityUtil.getAuthenticationIdentity(), AuthenticationIdentityEnum.class);
//        UserDetailsService userDetailsService = userDetailsServiceMap.get(clientId);
//
//        // 判断该客户端是否属于管理端
//        if (clientId.equals(Oauth2Constant.META_CLIENT_ADMIN_ID)) {
//            // 管理系统的用户体系是系统用户，认证方式通过用户名 username 认证
//            MetaAdminUserDetailsServiceImpl adminUserDetailsService = (MetaAdminUserDetailsServiceImpl) userDetailsService;
//
//            switch (authenticationIdentityEnum) {
//                case MOBILE -> {
//                    return adminUserDetailsService.loadUserByMobile(authentication.getName());
//                }
//                case SOCIAL -> {
//                    SocialAuthenticationToken authenticationToken = (SocialAuthenticationToken) authentication;
//                    return adminUserDetailsService.loadUserBySocial(authenticationToken.getName(), ((SocialAuthenticationToken) authentication).getSource());
//                }
//                case USERNAME -> {
//                    return adminUserDetailsService.loadUserByUsername(authentication.getName());
//                }
//                default -> throw new UsernameNotFoundException("暂不支持的认证类型");
//            }
//        } else {
//            // 管理系统的用户体系是系统用户，认证方式通过用户名 username 认证
//            MetaMemberUserDetailsServiceImpl memberUserDetailsService = (MetaMemberUserDetailsServiceImpl) userDetailsService;
//            switch (authenticationIdentityEnum) {
//                case MOBILE -> {
//                    return memberUserDetailsService.loadUserByMobile(authentication.getName());
//                }
//                case SOCIAL -> {
//                    SocialAuthenticationToken authenticationToken = (SocialAuthenticationToken) authentication;
//                    return memberUserDetailsService.loadUserBySocial(authenticationToken.getName(), ((SocialAuthenticationToken) authentication).getSource());
//                }
////                case WECHAT_APPLETS -> {
////                    SocialAuthenticationToken authenticationToken = (SocialAuthenticationToken) authentication;
////                    return memberUserDetailsService.loadUserBySocial(authenticationToken.getName(), ((SocialAuthenticationToken) authentication).getSource());
////                }
//                case DOUYIN_APPLET -> {
//                    DouyinAuthenticationToken authenticationToken = (DouyinAuthenticationToken) authentication;
//                    return memberUserDetailsService.loadUserBySocial(authenticationToken.getName(), ((DouyinAuthenticationToken) authentication).getSource());
//                }
//                case USERNAME -> {
//                    return memberUserDetailsService.loadUserByUsername(authentication.getName());
//                }
//                default -> throw new UsernameNotFoundException("暂不支持的认证类型");
//            }
//        }
//    }
//}
