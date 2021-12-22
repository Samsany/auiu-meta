package com.auiucloud.core.security.model;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

/**
 * @author dries
 * @date 2021/12/22
 */
@Getter
public class SecurityUser extends User {

    private static final long serialVersionUID = 4501920308335108690L;

    /**
     * 用户ID
     */
    private final Long id;

    /**
     * 部门ID
     */
    private final String roleId;
    /**
     * 部门ID
     */
    private final Long departId;

    /**
     * 手机号
     */
    private final String mobile;

    /**
     * 头像
     */
    private final String avatar;

    /**
     * 登录类型
     */
    private final int type;

    public SecurityUser(String username, String password, boolean enabled, boolean accountNonExpired, boolean credentialsNonExpired, boolean accountNonLocked, Collection<? extends GrantedAuthority> authorities, Long id, String roleId, Long departId, String mobile, String avatar, int type) {
        super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
        this.id = id;
        this.roleId = roleId;
        this.departId = departId;
        this.mobile = mobile;
        this.avatar = avatar;
        this.type = type;
    }

}
