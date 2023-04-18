package com.auiucloud.auth.extension.wechat;

import com.auiucloud.core.common.enums.AuthenticationIdentityEnum;
import com.auiucloud.core.security.service.MetaUserDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.HashSet;

/**
 * @author dries
 **/
@RequiredArgsConstructor
public class WechatAuthenticationProvider implements AuthenticationProvider {
    private final MetaUserDetailService userDetailsService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        WechatAuthenticationToken authenticationToken = (WechatAuthenticationToken) authentication;
        String username = (String) authenticationToken.getPrincipal();
        UserDetails userDetails = userDetailsService.loadUserBySocial(username, AuthenticationIdentityEnum.WECHAT_APPLET.getValue());

        WechatAuthenticationToken authenticationResult = new WechatAuthenticationToken(userDetails, new HashSet<>());
        authenticationResult.setDetails(authentication.getDetails());
        return authenticationResult;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return WechatAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
