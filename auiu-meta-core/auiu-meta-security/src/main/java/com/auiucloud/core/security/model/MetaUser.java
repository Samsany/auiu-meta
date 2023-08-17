package com.auiucloud.core.security.model;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.io.Serial;
import java.util.Collection;

/**
 * @author dries
 * @date 2021/12/22
 */
@Getter
public class MetaUser extends User {

    @Serial
    private static final long serialVersionUID = 4501920308335108690L;

    private final Long id;

    /**
     * 登录类型
     */
    private final String loginType;

    public MetaUser(Long id, String loginType, String username, String password, boolean enabled, boolean accountNonExpired, boolean credentialsNonExpired, boolean accountNonLocked, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
        this.id = id;
        this.loginType = loginType;
    }

    // public MetaUser(Long userId, String username, String password, Collection<? extends GrantedAuthority> authorities, boolean enabled, boolean accountNonExpired, boolean accountNonLocked, boolean credentialsNonExpired, String loginType) {
    //     this.userId = userId;
    //     this.username = username;
    //     this.password = password;
    //     this.enabled = enabled;
    //     this.accountNonExpired = accountNonExpired;
    //     this.credentialsNonExpired = credentialsNonExpired;
    //     this.accountNonLocked = accountNonLocked;
    //     this.authorities = authorities;
    //     this.loginType = loginType;
    // }

}
