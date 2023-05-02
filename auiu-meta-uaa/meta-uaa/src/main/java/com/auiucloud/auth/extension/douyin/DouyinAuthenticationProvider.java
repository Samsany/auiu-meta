package com.auiucloud.auth.extension.douyin;

import com.auiucloud.auth.extension.social.SocialAuthenticationToken;
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
public class DouyinAuthenticationProvider implements AuthenticationProvider {
    private final MetaUserDetailService userDetailsService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        DouyinAuthenticationToken authenticationToken = (DouyinAuthenticationToken) authentication;
        String username = (String) authenticationToken.getPrincipal();
        UserDetails userDetails = userDetailsService.loadUserBySocial(username, AuthenticationIdentityEnum.DOUYIN_APPLET.getValue());

        DouyinAuthenticationToken authenticationResult = new DouyinAuthenticationToken(userDetails, new HashSet<>());
        authenticationResult.setDetails(authentication.getDetails());
        return authenticationResult;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return DouyinAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
