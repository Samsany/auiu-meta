package com.auiucloud.core.feign.fallback;

import feign.Target;
import lombok.RequiredArgsConstructor;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cloud.openfeign.FallbackFactory;

/**
 * 默认fallback，减少必要的编写fallback类
 *
 * @author dries
 * @date 2021/12/22
 */
@RequiredArgsConstructor
public class MateFallbackFactory<T> implements FallbackFactory<T> {

    private final Target<T> target;

    @Override
    public T create(Throwable cause) {
        final Class<T> targetType = target.type();
        final String targetName = target.name();
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(targetType);
        enhancer.setUseCache(true);
        enhancer.setCallback(new MateFeignFallback<>(targetType, targetName, cause));
        return (T) enhancer.create();
    }
}
