package com.auiucloud.core.feign.fallback;

import com.auiucloud.core.common.api.ApiResult;
import com.auiucloud.core.common.api.ResultCode;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;
import org.springframework.util.ObjectUtils;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Objects;

/**
 * fallback 代理处理
 *
 * @author dries
 * @date 2021/12/22
 */
@Slf4j
@RequiredArgsConstructor
public class MateFeignFallback<T> implements MethodInterceptor {

    private final Class<T> targetType;
    private final String targetName;
    private final Throwable cause;
    private final String code = "code";

    @Override
    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
        String errorMessage = cause.getMessage();
        log.error("MateFeignFallback:[{}.{}] serviceId:[{}] message:[{}]", targetType.getName(), method.getName(), targetName, errorMessage);
        Class<?> returnType = method.getReturnType();
        // 暂时不支持 flux，rx，异步等，返回值不是 R，直接返回 null。
        if (ApiResult.class != returnType) {
            return null;
        }
        // 非 FeignException
        if (!(cause instanceof FeignException)) {
            return ApiResult.fail(ResultCode.FAILURE, errorMessage);
        }
        FeignException exception = (FeignException) cause;
        byte[] content = exception.content();
        // 如果返回的数据为空
        if (ObjectUtils.isEmpty(content)) {
            return ApiResult.fail(ResultCode.FAILURE, errorMessage);
        }
        return ApiResult.fail(Arrays.toString(content));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MateFeignFallback<?> that = (MateFeignFallback<?>) o;
        return targetType.equals(that.targetType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(targetType);
    }
}
