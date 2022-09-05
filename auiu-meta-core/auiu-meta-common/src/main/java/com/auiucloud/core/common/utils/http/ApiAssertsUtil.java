package com.auiucloud.core.common.utils.http;

import com.auiucloud.core.common.api.IResultCode;
import com.auiucloud.core.common.exception.ApiException;

/**
 * 断言处理工具类，用于抛出各种API异常
 *
 * @author dries
 * @date 2021/12/21
 */
public class ApiAssertsUtil {

    public static void fail(String message) {
        throw new ApiException(message);
    }

    public static void fail(IResultCode errorCode) {
        throw new ApiException(errorCode);
    }

}
