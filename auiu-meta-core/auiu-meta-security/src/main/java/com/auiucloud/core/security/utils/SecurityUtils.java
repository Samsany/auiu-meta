package com.auiucloud.core.security.utils;

import cn.hutool.core.util.StrUtil;
import com.auiucloud.core.common.constant.Oauth2Constant;
import com.auiucloud.core.security.model.MetaUser;
import lombok.experimental.UtilityClass;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author dries
 * @date 2023/8/15 14:43
 * @description
 **/
@UtilityClass
public class SecurityUtils {

    /**
     * 获取Authentication
     */
    public Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }


    /**
     * 获取用户信息
     */
    public MetaUser getUser(Authentication authentication) {
        Object principal = authentication.getPrincipal();
        if (principal instanceof MetaUser) {
            return (MetaUser) principal;
        }
        return null;
    }

    /**
     * 获取用户角色信息
     * @return 角色集合
     */
    public List<Long> getRoles() {
        Authentication authentication = getAuthentication();
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

        List<Long> roleIds = new ArrayList<>();
        authorities.parallelStream()
                .filter(granted -> StrUtil.startWith(granted.getAuthority(), Oauth2Constant.ROLE_PREFIX))
                .forEach(granted -> {
                    String id = StrUtil.removePrefix(granted.getAuthority(), Oauth2Constant.ROLE_PREFIX);
                    roleIds.add(Long.parseLong(id));
                });
        return roleIds;
    }

}
