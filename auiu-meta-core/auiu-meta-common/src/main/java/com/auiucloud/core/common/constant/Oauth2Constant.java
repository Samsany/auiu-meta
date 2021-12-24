package com.auiucloud.core.common.constant;

/**
 * @author dries
 * @date 2021/12/21
 */
public class Oauth2Constant {

    public static final String JWT_TOKEN_PREFIX = "Bearer ";
    public static final String JWT_TOKEN_HEADER = "Authorization";

    public static final String ALL = "/**";

    public static final String OAUTH_ALL = "/oauth/**";

    public static final String OAUTH_AUTHORIZE = "/oauth/authorize";

    public static final String OAUTH_CHECK_TOKEN = "/oauth/check_token";

    public static final String OAUTH_CONFIRM_ACCESS = "/oauth/confirm_access";

    public static final String OAUTH_TOKEN = "/oauth/token";

    public static final String OAUTH_TOKEN_KEY = "/oauth/token_key";

    public static final String OAUTH_ERROR = "/oauth/error";

    public static final String OAUTH_MOBILE = "/oauth/mobile";

    /**
     * 刷新模式
     */
    public static final String REFRESH_TOKEN = "refresh_token";
    /**
     * 授权码模式
     */
    public static final String AUTHORIZATION_CODE = "authorization_code";
    /**
     * 客户端模式
     */
    public static final String CLIENT_CREDENTIALS = "client_credentials";
    /**
     * 密码模式
     */
    public static final String PASSWORD = "password";
    /**
     * 简化模式
     */
    public static final String IMPLICIT = "implicit";

    /**
     * 发送短信验证码 或 验证短信验证码时，传递手机号的参数的名称
     */
    public static final String DEFAULT_PARAMETER_NAME_MOBILE = "mobile";

    /**
     * 社交登录，传递的参数名称
     */
    public static final String DEFAULT_PARAMETER_NAME_SOCIAL = "social";


    /**
     * 字段描述开始：用户ID
     */
    public static final String AUIU_MATE_USER_ID = "userId";

    /**
     * 用户名
     */
    public static final String AUIU_MATE_USER_NAME = "username";

    /**
     * 用户头像
     */
    public static final String AUIU_MATE_AVATAR = "avatar";

    /**
     * 用户权限ID
     */
    public static final String AUIU_MATE_ROLE_IDS = "roleIds";

    /**
     * 用户类型
     */
    public static final String AUIU_MATE_TYPE = "type";
}
