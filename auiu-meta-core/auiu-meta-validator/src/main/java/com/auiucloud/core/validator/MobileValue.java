package com.auiucloud.core.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @author dries
 * @date 2021/12/22
 */
@Documented
@Constraint(validatedBy = MobileValueValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface MobileValue {

    String message() default "手机号码格式不正确";

    Class[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    /**
     * 是否必填
     * <p>
     * 如果必填，在校验的时候本字段没值就会报错
     */
    boolean required() default true;

    @Target({ElementType.FIELD, ElementType.PARAMETER})
    @Retention(RUNTIME)
    @Documented
    @interface List {
        MobileValue[] value();
    }

}
