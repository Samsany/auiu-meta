package com.auiucloud.core.common.context;

import com.auiucloud.core.common.domain.LoginUser;

/**
 * 用户上下文
 *
 * @author dries
 * @date 2021/12/20
 */
public class UserContext {

    private static final ThreadLocal<LoginUser> USER_HOLDER = new ThreadLocal<>();

    public static void setUser(LoginUser loginUser) {
        USER_HOLDER.set(loginUser);
    }

    public static LoginUser getUser() {
        return USER_HOLDER.get();
    }

    public static void removeUser() {
        USER_HOLDER.remove();
    }
}
