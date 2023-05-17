package com.auiucloud.core.security.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * 获取用户信息扩展
 *
 * @author dries
 * @date 2021/12/22
 */
public interface MetaUserDetailService extends UserDetailsService {

    /**
     * 根据手机号登录
     *
     * @param mobile 手机号
     * @return UserDetails
     * @throws UsernameNotFoundException 用户不存在抛异常
     */
    UserDetails loadUserByMobile(String mobile) throws UsernameNotFoundException;

    /**
     * 根据社交账号登录
     *
     * @param username 第三方绑定的用户账户
     * @param source   第三方
     * @return UserDetails
     * @throws UsernameNotFoundException 用户不存在抛异常
     */
    UserDetails loadUserBySocial(String username, Integer source) throws UsernameNotFoundException;

}
