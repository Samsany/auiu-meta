package com.auiucloud.core.common.utils;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.auiucloud.core.common.constant.Oauth2Constant;
import com.auiucloud.core.common.context.UserContext;
import com.auiucloud.core.common.exception.ApiException;
import com.auiucloud.core.common.exception.TokenException;
import com.auiucloud.core.common.model.LoginUser;
import com.nimbusds.jose.JWSObject;
import lombok.extern.slf4j.Slf4j;

import java.text.ParseException;
import java.util.Set;

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

    public static String getRealToken() {
        String token = getHeaderToken();
        return token.replace(Oauth2Constant.JWT_TOKEN_PREFIX, "");
    }

    public static JSONObject getJwtPayload() {
        // 从token中解析用户信息
        String realToken = getRealToken();
        return getJwtPayload(realToken);
    }

    public static JSONObject getJwtPayload(String realToken) {
        // 从token中解析用户信息
        JWSObject jwsObject;
        try {
            jwsObject = JWSObject.parse(realToken);
        } catch (ParseException ex) {
            log.error("解析用户信息异常: {}", ex.getMessage());
            throw new ApiException(ex.getMessage());
        }
        String payload = jwsObject.getPayload().toString();
        return JSONUtil.parseObj(payload);
    }

    public static Long getUserId() {
        return getJwtPayload().getLong(Oauth2Constant.META_USER_ID);
    }

    public static String getUsername() {
        return getJwtPayload().getStr(Oauth2Constant.META_USER_NAME);
    }

    /**
     * 从HttpServletRequest获取LoginUser信息
     *
     * @return LoginUser
     */
    public static LoginUser getUser() {
        // 然后根据token获取用户登录信息，这里省略获取用户信息的过程
        JSONObject jwtPayload = getJwtPayload();
        LoginUser loginUser = new LoginUser();
        loginUser.setUserId(jwtPayload.getStr(Oauth2Constant.META_USER_NAME));
        loginUser.setAccount(jwtPayload.getStr(Oauth2Constant.META_USER_NAME));
        loginUser.setRoleIds((Set<String>) jwtPayload.get(Oauth2Constant.META_ROLE_IDS));
        loginUser.setType(jwtPayload.getInt(Oauth2Constant.META_TYPE));
        UserContext.setUser(loginUser);
        return loginUser;
    }

}
