package com.auiucloud.core.security.service;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.system.UserInfo;
import com.auiucloud.core.security.model.MetaUser;
import org.springframework.core.Ordered;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * 获取用户信息扩展
 *
 * @author dries
 * @date 2021/12/22
 */
public interface MetaUserDetailService extends UserDetailsService, Ordered {

    /**
     * 是否支持此客户端校验
     * @param clientId 目标客户端
     * @return true/false
     */
    default boolean support(String clientId, String grantType) {
        return true;
    }

    /**
     * 排序值 默认取最大的
     * @return 排序值
     */
    default int getOrder() {
        return 0;
    }

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
     * @param source 第三方
     * @return UserDetails
     * @throws UsernameNotFoundException 用户不存在抛异常
     */
    UserDetails loadUserBySocial(String username, Integer source) throws UsernameNotFoundException;

}
