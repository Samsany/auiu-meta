package com.auiucloud.core.common.utils;

import com.auiucloud.core.common.exception.ApiException;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Set;

/**
 * @author dries
 * @createDate 2022-06-30 23-51
 */
public class ValidatorUtils {

    private static final Validator validator;

    static {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    /**
     * 校验对象
     *
     * @param object 待校验对象
     * @param groups 待校验的组
     * @throws ApiException 校验不通过，则报 ApiException异常
     */
    public static void validateEntity(Object object, Class<?>... groups)
            throws ApiException {
        Set<ConstraintViolation<Object>> constraintViolations = validator.validate(object, groups);
        if (!constraintViolations.isEmpty()) {
            StringBuilder msg = new StringBuilder();
            for (ConstraintViolation<Object> constraint : constraintViolations) {
                msg.append(constraint.getMessage()).append("<br>");
            }
            throw new ApiException(msg.toString());
        }
    }

}
