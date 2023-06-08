package com.auiucloud.core.common.utils;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.auiucloud.core.common.constant.CommonConstant;
import com.auiucloud.core.common.constant.Oauth2Constant;
import com.auiucloud.core.common.context.UserContext;
import com.auiucloud.core.common.exception.TokenException;
import com.auiucloud.core.common.model.LoginUser;
import com.auiucloud.core.common.utils.http.RequestHolder;
import com.nimbusds.jose.JWSObject;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.util.Base64;
import java.util.List;

/**
 * Security工具类
 *
 * @author dries
 * @date 2021/12/24
 */
@Slf4j
public class SecurityUtil {

    /**
     * 从HttpServletRequest里获取token
     *
     * @return token
     */
    public static String getHeaderToken() {
        String token = RequestHolder.getHttpServletRequestHeader(Oauth2Constant.JWT_TOKEN_HEADER);
        if (StrUtil.isBlank(token)) {
            throw new TokenException("没有携带Token信息！");
        }
        return token;
    }

    /**
     * 从HttpServletRequest里获取access_token, 去除token前缀
     *
     * @return token
     */
    public static String getRealToken() {
        String token = getHeaderToken();
        return token.replace(Oauth2Constant.JWT_TOKEN_PREFIX, "");
    }

    /**
     * 从token中解析用户信息
     *
     * @return JSONObject
     */
    public static JSONObject getJwtPayload() {
        // 从token中解析用户信息
        String realToken = getRealToken();
        return getJwtPayload(realToken);
    }

    /**
     * 从token中解析用户信息
     *
     * @param realToken token
     * @return JSONObject
     */
    public static JSONObject getJwtPayload(String realToken) {
        // 从token中解析用户信息
        JWSObject jwsObject;
        try {
            jwsObject = JWSObject.parse(realToken);
        } catch (ParseException ex) {
            log.error("解析用户信息异常: {}", ex.getMessage());
            throw new TokenException(ex.getMessage());
        }
        String payload = jwsObject.getPayload().toString();
        return JSONUtil.parseObj(payload);
    }

    /**
     * 获取用户ID
     *
     * @return ID
     */
    public static Long getUserId() {
        return getJwtPayload().getLong(Oauth2Constant.META_USER_ID);
    }

    /**
     * 获取用户登录类型
     *
     * @return ID
     */
    public static String getLoginType() {
        return getJwtPayload().getStr(Oauth2Constant.META_LOGIN_TYPE);
    }

    /**
     * 获取用户ID 默认返回NULL
     *
     * @return ID
     */
    public static Long getUserIdOrDefault() {
        try {
            return getJwtPayload().getLong(Oauth2Constant.META_USER_ID);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 获取用户账户
     *
     * @return 账户
     */
    public static String getUsername() {
        return getJwtPayload().getStr(Oauth2Constant.META_USER_NAME);
    }

    /**
     * 获取用户登录客户端
     *
     * @return 客户端
     */
    public static String getLoginClient() {
        return getJwtPayload().getStr(Oauth2Constant.META_LOGIN_CLIENT);
    }


    /**
     * 获取部门ID
     *
     * @return deptId
     */
    public static Long getDeptId() {
        // 然后根据token获取用户登录信息，这里省略获取用户信息的过程
        JSONObject jwtPayload = getJwtPayload();

        // 获取部门ID
        return jwtPayload.getLong(Oauth2Constant.META_DEPT_ID);
    }

    /**
     * 获取用户角色
     *
     * @return 角色Code集合
     */
    public static List<String> getRoles() {
        // 然后根据token获取用户登录信息，这里省略获取用户信息的过程
        JSONObject jwtPayload = getJwtPayload();

        // 获取角色信息
        String rolesStr = jwtPayload.getStr(Oauth2Constant.META_ROLES);
        return JSONUtil.toList(rolesStr, String.class);
    }

    /**
     * 从HttpServletRequest获取LoginUser信息
     *
     * @return LoginUser
     */
    public static LoginUser getUser() {
        // 然后根据token获取用户登录信息，这里省略获取用户信息的过程
        JSONObject jwtPayload = getJwtPayload();

        // 获取角色信息
        String rolesStr = jwtPayload.getStr(Oauth2Constant.META_ROLES);
        List<String> roles = JSONUtil.toList(rolesStr, String.class);

        LoginUser loginUser = LoginUser.builder()
                .userId(jwtPayload.getLong(Oauth2Constant.META_USER_ID))
                .account(jwtPayload.getStr(Oauth2Constant.META_USER_NAME))
                .roles(roles)
                .loginType(jwtPayload.getInt(Oauth2Constant.META_LOGIN_TYPE))
                .build();
        UserContext.setUser(loginUser);
        return loginUser;
    }

    /**
     * 获取登录认证的客户端ID
     * <p>
     * 兼容两种方式获取OAuth2客户端信息（client_id、client_secret）
     * 方式一：client_id、client_secret放在请求路径中
     * 方式二：放在请求头（Request Headers）中的Authorization字段，且经过加密，例如 Basic Y2xpZW50OnNlY3JldA== 明文等于 client:secret
     *
     * @return
     */
    @SneakyThrows
    public static String getOAuth2ClientId() {

        HttpServletRequest request = RequestHolder.getHttpServletRequest();

        // 从请求路径中获取
        String clientId = request.getParameter("client_id");
        if (StrUtil.isNotBlank(clientId)) {
            return clientId;
        }

        // 从请求头获取
        String basic = request.getHeader("Authorization");
        if (StrUtil.isNotBlank(basic) && basic.startsWith("Basic ")) {
            basic = basic.replace("Basic ", "");
            String basicPlainText = new String(Base64.getDecoder().decode(basic.getBytes(StandardCharsets.UTF_8)), StandardCharsets.UTF_8);
            clientId = basicPlainText.split(":")[0]; // client:secret
        }
        return clientId;
    }

    /**
     * 解析JWT获取认证类型
     *
     * @return
     */
    @SneakyThrows
    public static Integer getAuthenticationIdentity() {
        HttpServletRequest request = RequestHolder.getHttpServletRequest();
        String refreshToken = request.getParameter("refresh_token");

        String payload = StrUtil.toString(JWSObject.parse(refreshToken).getPayload());
        JSONObject jsonObject = JSONUtil.parseObj(payload);

        return jsonObject.getInt(Oauth2Constant.META_LOGIN_TYPE, CommonConstant.STATUS_DISABLE_VALUE);
    }

}
