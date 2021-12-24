package com.auiucloud.core.validator;

import cn.hutool.core.lang.Validator;
import cn.hutool.core.util.ReUtil;
import cn.hutool.core.util.StrUtil;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @author dries
 * @date 2021/12/22
 */
public class MobileValueValidator implements ConstraintValidator<MobileValue, String> {

    private Boolean required;

    @Override
    public void initialize(MobileValue constraintAnnotation) {
        this.required = constraintAnnotation.required();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
        if (StrUtil.isEmpty(value)) {
            if (required) {
                return false;
            } else {
                return true;
            }
        } else {
            return ReUtil.isMatch(Validator.MOBILE, value);
        }
    }

}
