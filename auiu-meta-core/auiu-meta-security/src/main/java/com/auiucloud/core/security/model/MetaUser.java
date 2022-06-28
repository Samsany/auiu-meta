package com.auiucloud.core.security.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;
import java.util.List;

/**
 * @author dries
 * @date 2021/12/22
 */
@Getter
@Setter
public class MetaUser extends User {

    private static final long serialVersionUID = 4501920308335108690L;

    /**
     * 用户ID
     */
    private final Long id;

    /**
     * 角色ID
     */
    private final List<String> roles;
    /**
     * 部门ID
     */
    private final Long deptId;

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

    public MetaUser(Long id, String username, Long deptId, List<String> roles, String mobile, String avatar, String password, int type, boolean enabled, boolean accountNonExpired, boolean credentialsNonExpired, boolean accountNonLocked, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
        this.id = id;
        this.roles = roles;
        this.deptId = deptId;
        this.mobile = mobile;
        this.avatar = avatar;
        this.type = type;
    }

}
