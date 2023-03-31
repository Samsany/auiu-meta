package com.auiucloud.auth.props;

/**
 * @author dries
 **/
public class DouyinAppletProps {

    // getAccessToken请求地址
    // 沙盒地址
    public static final String GET_ACCESS_TOKEN_URL_DEV = "https://open-sandbox.douyin.com/api/apps/v2/token";
    // 生产地址
    public static final String GET_ACCESS_TOKEN_URL_PROD = "https://developer.toutiao.com/api/apps/v2/token";

    // jscode2session请求地址
    public static final String GET_CODE_SESSION_URL_DEV = "https://open-sandbox.douyin.com/api/apps/v2/jscode2session";
    public static final String GET_CODE_SESSION_URL_PROD = "https://developer.toutiao.com/api/apps/v2/jscode2session";

}
