package com.auiucloud.core.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @author dries
 * @date 2021/12/22
 */
@Documented
@Constraint(validatedBy = DateValueValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface DateValue {

    String message() default "日期格式不正确，正确格式应为yyyy-MM-dd";

    Class[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    /**
     * 日期校验的格式，默认 yyyy-MM-dd
     */
    String format() default "yyyy-MM-dd";

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
        DateValue[] value();
    }

}
