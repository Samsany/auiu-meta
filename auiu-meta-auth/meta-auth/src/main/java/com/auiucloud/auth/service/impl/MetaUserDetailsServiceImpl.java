package com.auiucloud.auth.service.impl;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ObjectUtil;
import com.auiucloud.admin.feign.ISysUserProvider;
import com.auiucloud.admin.modules.system.domain.SysUser;
import com.auiucloud.admin.modules.system.vo.UserInfoVO;
import com.auiucloud.auth.domain.MetaClientDetails;
import com.auiucloud.auth.enums.Oauth2ClientTypeEnum;
import com.auiucloud.core.common.api.ApiResult;
import com.auiucloud.core.common.api.ResultCode;
import com.auiucloud.core.common.constant.CommonConstant;
import com.auiucloud.core.common.constant.RedisKeyConstant;
import com.auiucloud.core.common.enums.AuthenticationIdentityEnum;
import com.auiucloud.core.common.enums.IBaseEnum;
import com.auiucloud.core.common.exception.TokenException;
import com.auiucloud.core.redis.core.RedisService;
import com.auiucloud.core.security.model.MetaUser;
import com.auiucloud.core.security.service.MetaUserDetailService;
import com.auiucloud.ums.feign.IMemberProvider;
import com.auiucloud.ums.vo.MemberInfoVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.List;

/**
 * @author dries
 * @date 2022/2/9
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class MetaUserDetailsServiceImpl implements MetaUserDetailService {

    @Resource
    private ISysUserProvider sysUserProvider;
    @Resource
    private IMemberProvider memberProvider;
    private final RedisService redisService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String clientId = authentication.getName();
        log.info("客户端ID：{}", clientId);
        MetaClientDetails clientDetails = (MetaClientDetails) redisService.get(RedisKeyConstant.cacheClientKey(clientId));

        Oauth2ClientTypeEnum clientTypeEnum = IBaseEnum.getEnumByValue(clientDetails.getClientType(), Oauth2ClientTypeEnum.class);
        UserDetails userDetails = null;
        switch (clientTypeEnum) {
            case ADMIN -> {
                ApiResult<UserInfoVO> apiResult = sysUserProvider.getUserByUsername(username);
                if (apiResult.successful() && ObjectUtil.isNotNull(apiResult.getData())) {
                    UserInfoVO userInfo = apiResult.getData();
                    userInfo.setLoginType(String.valueOf(AuthenticationIdentityEnum.USERNAME.getValue()));
                    userDetails = buildUserDetails(userInfo);
                }
                if (ObjectUtil.isNull(userDetails)) {
                    throw new TokenException(ResultCode.USER_ERROR_A0201.getMessage());
                }
            }
            case MEMBER -> {
                ApiResult<MemberInfoVO> apiResult = memberProvider.getUserByUsername(username);
                if (apiResult.successful() && ObjectUtil.isNotNull(apiResult.getData())) {
                    MemberInfoVO userInfo = apiResult.getData();
                    userInfo.setLoginType(String.valueOf(AuthenticationIdentityEnum.USERNAME.getValue()));
                    userInfo.setAccount(username);
                    userDetails = buildUserDetails(userInfo);
                }
                if (ObjectUtil.isNull(userDetails)) {
                    throw new TokenException(ResultCode.USER_ERROR_A0201.getMessage());
                }
                userDetails = buildUserDetails(apiResult.getData());
            }
            default -> throw new TokenException("暂不支持的客户端类型：" + clientId);
        }

        return userDetails;
    }

    @Override
    public UserDetails loadUserByMobile(String mobile) throws UsernameNotFoundException {
        return null;
    }

    @Override
    public UserDetails loadUserBySocial(String username, Integer source) throws UsernameNotFoundException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String clientId = authentication.getName();
        log.info("客户端ID：{}", clientId);
        MetaClientDetails clientDetails = (MetaClientDetails) redisService.get(RedisKeyConstant.cacheClientKey(clientId));
        Oauth2ClientTypeEnum clientTypeEnum = IBaseEnum.getEnumByValue(clientDetails.getClientType(), Oauth2ClientTypeEnum.class);
        UserDetails userDetails = null;
        switch (clientTypeEnum) {
            case ADMIN -> {
                ApiResult<UserInfoVO> apiResult = sysUserProvider.getUserByUsername(username);
                if (apiResult.successful() && ObjectUtil.isNotNull(apiResult.getData())) {
                    UserInfoVO userInfo = apiResult.getData();
                    userInfo.setLoginType(String.valueOf(source));
                    userDetails = buildUserDetails(userInfo);
                }
                if (ObjectUtil.isNull(userDetails)) {
                    throw new TokenException(ResultCode.USER_ERROR_A0201.getMessage());
                }
            }
            case MEMBER -> {
                ApiResult<MemberInfoVO> apiResult = memberProvider.getUserByUsername(username);
                if (apiResult.successful() && ObjectUtil.isNotNull(apiResult.getData())) {
                    MemberInfoVO userInfo = apiResult.getData();
                    userInfo.setLoginType(String.valueOf(source));
                    userDetails = buildUserDetails(userInfo);
                }
                if (ObjectUtil.isNull(userDetails)) {
                    throw new TokenException(ResultCode.USER_ERROR_A0201.getMessage());
                }
            }
            default -> throw new TokenException("暂不支持的客户端类型：" + clientId);
        }

        return userDetails;
    }

    private UserDetails buildUserDetails(UserInfoVO userInfo) {
        if (ObjectUtil.isNull(userInfo)) {
            throw new TokenException("该用户：" + userInfo.getUsername() + "不存在");
        }
        SysUser user = userInfo.getSysUser();
        log.info("用户名：{}", userInfo.getSysUser().getAccount());
        Collection<? extends GrantedAuthority> authorities
                = AuthorityUtils.createAuthorityList(Convert.toStrArray(userInfo.getRoles()));
        log.info("authorities: {}", authorities);

        return new MetaUser(
                user.getId(),
                user.getAccount(),
                user.getPassword(),
                authorities,
                user.getStatus() == CommonConstant.STATUS_NORMAL_VALUE,
                user.isAccountNonExpired(),
                user.isCredentialsNonExpired(),
                user.isAccountNonLocked(),
                userInfo.getLoginType());
    }

    private UserDetails buildUserDetails(MemberInfoVO userInfo) {
        if (ObjectUtil.isNull(userInfo)) {
            throw new TokenException("该用户：" + userInfo.getAccount() + "不存在");
        }
        log.info("用户名：{}", userInfo.getAccount());
        List<String> roles = List.of("MEMBER");
        Collection<? extends GrantedAuthority> authorities
                = AuthorityUtils.createAuthorityList(Convert.toStrArray(roles));
        log.info("authorities: {}", authorities);
        return new MetaUser(
                userInfo.getUserId(),
                userInfo.getAccount(),
                userInfo.getPassword(),
                authorities,
                userInfo.getStatus() == CommonConstant.STATUS_NORMAL_VALUE,
                userInfo.isAccountNonExpired(),
                userInfo.isCredentialsNonExpired(),
                userInfo.isAccountNonLocked(),
                userInfo.getLoginType());
    }

}
