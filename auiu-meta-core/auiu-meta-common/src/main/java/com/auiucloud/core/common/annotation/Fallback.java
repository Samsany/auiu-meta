package com.auiucloud.core.common.annotation;

import org.springframework.core.annotation.AliasFor;
import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * Feign Fallback注解
 *
 * @author dries
 * @date 2021/12/21
 */
@Component
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Fallback {

    @AliasFor(annotation = Component.class)
    String value() default "";

}
