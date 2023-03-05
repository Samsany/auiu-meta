package com.auiucloud.core.common.constant;

/**
 * @author dries
 * @date 2021/12/21
 */
public class Oauth2Constant {

    public static final String META_CLIENT_ADMIN_ID = "meta-admin-client";
    public static final String APP_CLIENT_ID = "app";

    public static final String GRANT_TYPE_CAPTCHA = "captcha";
    public static final String GRANT_TYPE_SMS = "sms";
    public static final String GRANT_TYPE_SOCIAL = "social";

    public static final String JWT_TOKEN_PREFIX = "Bearer ";
    public static final String JWT_TOKEN_HEADER = "Authorization";
    /**
     * 认证身份标识
     */
    public static final String AUTHENTICATION_IDENTITY_KEY = "authenticationIdentity";

    /**
     * JWT存储权限属性
     */
    public static final String ROLE_PREFIX = "ROLE_";
    public static final String AUTHORITY_CLAIM_NAME = "authorities";

    public static final int LOGIN_USERNAME_TYPE = 1;
    public static final int LOGIN_MOBILE_TYPE = 2;

    public static final String ALL = "/**";
    public static final String OAUTH_ALL = "/oauth/**";
    public static final String OAUTH_AUTHORIZE = "/oauth/authorize";
    public static final String OAUTH_CHECK_TOKEN = "/oauth/check_token";
    public static final String OAUTH_CONFIRM_ACCESS = "/oauth/confirm_access";
    public static final String OAUTH_TOKEN = "/oauth/token";
    public static final String OAUTH_TOKEN_KEY = "/oauth/token_key";
    public static final String OAUTH_ERROR = "/oauth/error";
    public static final String OAUTH_MOBILE = "/oauth/mobile";
    public static final String OAUTH_SOCIAL = "/oauth/social/**";

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
     * 验证图片验证码时，http请求中默认的携带图片验证码信息的参数的名称
     */
    public static final String DEFAULT_PARAMETER_NAME_CODE_IMAGE = "imageCode";
    /**
     * 验证短信验证码时，http请求中默认的携带短信验证码信息的参数的名称
     */
    public static final String DEFAULT_PARAMETER_NAME_CODE_SMS = "smsCode";
    /**
     * 发送短信验证码 或 验证短信验证码时，传递手机号的参数的名称
     */
    public static final String DEFAULT_PARAMETER_NAME_MOBILE = "mobile";
    public static final String CAPTCHA_PROCESSOR_SEPARATOR = "CodeProcessor";

    /**
     * 社交登录，传递的参数名称
     */
    public static final String DEFAULT_PARAMETER_NAME_SOCIAL = "social";

    /**
     * 字段描述开始：用户
     */
    public static final String META_USER = "user";
    /**
     * 字段描述开始：用户ID
     */
    public static final String META_USER_ID = "userId";

    /**
     * 用户名
     */
    public static final String META_USER_NAME = "username";

    /**
     * 用户昵称
     */
    public static final String META_NICK_NAME = "nickname";

    /**
     * 用户头像
     */
    public static final String META_AVATAR = "avatar";

    /**
     * 用户角色编码
     */
    public static final String META_ROLES = "roles";

    /**
     * 用户类型
     */
    public static final String META_TYPE = "type";

    /**
     * JWT唯一标识
     */
    public static final String META_USER_JTI = "jti";

    /*** 自定义客户端client表名 */
    public static final String META_CLIENT_TABLE = "sys_oauth_client";
    /**
     * 基础查询语句
     */
    public static final String META_CLIENT_BASE = "select client_id, client_secret, resource_ids, scope, " +
            "authorized_grant_types, web_server_redirect_uri, authorities, access_token_validity," +
            "refresh_token_validity, additional_information, auto_approve as autoapprove from " + META_CLIENT_TABLE;
    public static final String FIND_CLIENT_DETAIL_SQL = META_CLIENT_BASE + " order by client_id";
    public static final String SELECT_CLIENT_DETAIL_SQL = META_CLIENT_BASE + " where client_id = ?";

    /**
     * 标志
     */
    public static final String FROM = "from";

    /**
     * 内部
     */
    public static final String FROM_IN = "Y";

}
