package com.auiucloud.auth.extension.social;

import com.auiucloud.core.common.enums.AuthenticationIdentityEnum;
import com.auiucloud.core.security.service.MetaUserDetailService;
import lombok.RequiredArgsConstructor;
import me.zhyd.oauth.model.AuthUser;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;

import javax.annotation.Resource;
import java.util.Objects;

/**
 * @author dries
 **/
@RequiredArgsConstructor
public class SocialAuthenticationProvider implements AuthenticationProvider {

    private final MetaUserDetailService userDetailsService;
    @Resource
    private SocialConfig socialConfig;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        SocialAuthenticationToken authenticationToken = (SocialAuthenticationToken) authentication;
        String username = ((AuthUser) authenticationToken.getPrincipal()).getUsername();
        UserDetails userDetails = userDetailsService.loadUserBySocial(username, AuthenticationIdentityEnum.SOCIAL.getValue());

        if (Objects.isNull(userDetails)) {
            // todo 获取前端参数 重新跳转回去注册
            throw new InternalAuthenticationServiceException("社交登录错误");
        }

        SocialAuthenticationToken authenticationResult = new SocialAuthenticationToken(userDetails, userDetails.getAuthorities());

        authenticationResult.setDetails(authenticationToken.getDetails());

        return authenticationResult;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return SocialAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
