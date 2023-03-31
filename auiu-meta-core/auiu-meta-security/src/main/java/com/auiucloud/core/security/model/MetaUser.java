package com.auiucloud.core.security.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serial;
import java.util.Collection;

/**
 * @author dries
 * @date 2021/12/22
 */
@Getter
public class MetaUser implements UserDetails {

    @Serial
    private static final long serialVersionUID = 4501920308335108690L;

    private final Long userId;
    private final String username;
    private final String password;
    private final Collection<? extends GrantedAuthority> authorities;
    private final boolean accountNonExpired;
    private final boolean accountNonLocked;
    private final boolean credentialsNonExpired;
    private final boolean enabled;
    /**
     * 登录类型
     */
    private final Integer loginType;

    public MetaUser(Long userId, String username, String password, Collection<? extends GrantedAuthority> authorities, Integer loginType) {
        this(userId, username, password, authorities, true, true, true, true, loginType);
    }

    public MetaUser(Long userId, String username, String password, Collection<? extends GrantedAuthority> authorities, boolean enabled, boolean accountNonExpired, boolean accountNonLocked, boolean credentialsNonExpired, Integer loginType) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.enabled = enabled;
        this.accountNonExpired = accountNonExpired;
        this.credentialsNonExpired = credentialsNonExpired;
        this.accountNonLocked = accountNonLocked;
        this.authorities = authorities;
        this.loginType = loginType;
    }

    //    public MetaUser(Long id,
//                    String username,
//                    Long deptId,
//                    List<String> roles,
//                    String mobile,
//                    String avatar,
//                    String password,
//                    int type,
//                    boolean enabled,
//                    boolean accountNonExpired, boolean credentialsNonExpired, boolean accountNonLocked,
//                    Collection<? extends GrantedAuthority> authorities) {
//        super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
//        this.id = id;
//        this.roles = roles;
//        this.deptId = deptId;
//        this.mobile = mobile;
//        this.avatar = avatar;
//        this.type = type;
//    }
//
//    public MetaUser(Long id,
//                    String username,
//                    String openId,
//                    List<String> roles,
//                    String mobile,
//                    String avatar,
//                    String password,
//                    int type,
//                    boolean enabled, boolean accountNonExpired, boolean credentialsNonExpired, boolean accountNonLocked,
//                    Collection<? extends GrantedAuthority> authorities) {
//        this.id = id;
//        this.roles = roles;
//        this.openId = openId;
//        this.mobile = mobile;
//        this.avatar = avatar;
//        this.type = type;
//    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return this.accountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return this.accountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return this.credentialsNonExpired;
    }

    @Override
    public boolean isEnabled() {
        return this.enabled;
    }
}
