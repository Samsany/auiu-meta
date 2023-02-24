package com.auiucloud.auth.service.impl;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ObjectUtil;
import com.auiucloud.admin.domain.SysUser;
import com.auiucloud.admin.dto.SysUserInfo;
import com.auiucloud.admin.feign.ISysUserProvider;
import com.auiucloud.core.common.api.ApiResult;
import com.auiucloud.core.common.api.ResultCode;
import com.auiucloud.core.common.constant.CommonConstant;
import com.auiucloud.core.common.constant.Oauth2Constant;
import com.auiucloud.core.common.exception.TokenException;
import com.auiucloud.core.security.model.MetaUser;
import com.auiucloud.core.security.service.MetaUserDetailService;
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

/**
 * @author dries
 * @date 2022/2/9
 */
@Slf4j
@Service
public class UserDetailsServiceImpl implements MetaUserDetailService {

    @Resource
    private ISysUserProvider sysUserProvider;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String clientId = authentication.getName();
        log.info("客户端ID：{}", clientId);

        UserDetails userDetails = null;
        if (Oauth2Constant.META_CLIENT_ADMIN_ID.equals(clientId)) {
            ApiResult<SysUserInfo> result = sysUserProvider.getUserByUsername(username);
            if (result.successful() && ObjectUtil.isNotNull(result.getData())) {
                SysUserInfo userInfo = result.getData();
                userInfo.setType(Oauth2Constant.LOGIN_USERNAME_TYPE);
                userInfo.setUsername(username);
                userDetails = buildUserDetails(userInfo);
            }
            if (ObjectUtil.isNull(userDetails)) {
                throw new TokenException(ResultCode.USER_ERROR_A0201.getMessage());
            }
            return userDetails;
        } else {
            throw new TokenException("暂不支持的客户端：" + clientId);
        }
    }

    @Override
    public UserDetails loadUserByMobile(String mobile) throws UsernameNotFoundException {
        return null;
    }

    @Override
    public UserDetails loadUserBySocial(String openId) throws UsernameNotFoundException {
        return null;
    }

    private UserDetails buildUserDetails(SysUserInfo userInfo) {
        if (ObjectUtil.isNull(userInfo)) {
            throw new TokenException("该用户：" + userInfo.getUsername() + "不存在");
        }
        SysUser user = userInfo.getSysUser();
        log.info("用户名：{}", userInfo.getSysUser().getAccount());
        Collection<? extends GrantedAuthority> authorities
                = AuthorityUtils.createAuthorityList(Convert.toStrArray(userInfo.getRoles()));
        log.info("authorities: {}", authorities);
        return new MetaUser(user.getId(),
                user.getAccount(),
                user.getDeptId(),
                userInfo.getRoles(),
                user.getMobile(),
                user.getAvatar(),
                user.getPassword(),
                userInfo.getType(),
                user.getStatus() == CommonConstant.STATUS_NORMAL_VALUE,
                user.isAccountNonExpired(),
                user.isCredentialsNonExpired(),
                user.isAccountNonLocked(),
                authorities);
    }

}
