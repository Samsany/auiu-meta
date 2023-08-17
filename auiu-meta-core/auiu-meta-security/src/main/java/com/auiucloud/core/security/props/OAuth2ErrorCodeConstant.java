package com.auiucloud.core.security.props;

import lombok.experimental.UtilityClass;

/**
 * @author dries
 * @date 2023/8/15 14:32
 * @description
 **/
@UtilityClass
public class OAuth2ErrorCodeConstant {

    /**
     * 用户名未找到
     */
    public String USERNAME_NOT_FOUND = "username_not_found";

    /**
     * 错误凭证
     */
    public String BAD_CREDENTIALS = "bad_credentials";

    /**
     * 用户被锁
     */
    public String USER_LOCKED = "user_locked";

    /**
     * 用户禁用
     */
    public String USER_DISABLE = "user_disable";

    /**
     * 用户过期
     */
    public String USER_EXPIRED = "user_expired";

    /**
     * 证书过期
     */
    public String CREDENTIALS_EXPIRED = "credentials_expired";

    /**
     * scope 为空异常
     */
    public String SCOPE_IS_EMPTY = "scope_is_empty";

    /**
     * 令牌不存在
     */
    public String TOKEN_MISSING = "token_missing";

    /**
     * 未知的登录异常
     */
    public String UN_KNOW_LOGIN_ERROR = "un_know_login_error";

    /**
     * 不合法的Token
     */
    public String INVALID_BEARER_TOKEN = "invalid_bearer_token";

}
