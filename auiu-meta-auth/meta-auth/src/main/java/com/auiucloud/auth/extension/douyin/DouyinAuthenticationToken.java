package com.auiucloud.auth.extension.douyin;

import lombok.Getter;
import lombok.SneakyThrows;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.util.Assert;

import java.io.Serial;
import java.util.Collection;

/**
 * @author dries
 **/
public class DouyinAuthenticationToken extends AbstractAuthenticationToken {

    @Serial
    private static final long serialVersionUID = -8646157830173092716L;

    private final Object principal;

    /**
     * 账号校验之前的token构建
     *
     * @param principal
     */
    public DouyinAuthenticationToken(Object principal) {
        super(null);
        this.principal = principal;
    }

    /**
     * 账号校验成功之后的token构建
     *
     * @param principal
     * @param authorities
     */
    public DouyinAuthenticationToken(Object principal, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.principal = principal;
        super.setAuthenticated(true);
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return this.principal;
    }

    @Override
    @SneakyThrows
    public void setAuthenticated(boolean isAuthenticated) {
        Assert.isTrue(isAuthenticated, "Cannot set this token to trusted - use constructor which takes a GrantedAuthority list instead");
        super.setAuthenticated(false);
    }

    public void eraseCredentials() {
        super.eraseCredentials();
    }
}
