package com.auiucloud.core.validator;

import com.auiucloud.core.common.enums.StatusEnum;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * 校验状态，判断是否为 StatusEnum 中的值
 *
 * @author dries
 * @date 2021/12/22
 */
public class StatusValueValidator implements ConstraintValidator<StatusValue, String> {

    private Boolean required;

    @Override
    public void initialize(StatusValue constraintAnnotation) {
        this.required = constraintAnnotation.required();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
        // 如果是必填的
        if (required && value == null) {
            return false;
        }

        // 如果不是必填，为空的话就通过
        if (!required && value == null) {
            return true;
        }

        // 校验值是否是枚举中的值
        StatusEnum statusEnum = StatusEnum.codeToEnum(value);
        return statusEnum != null;
    }
}
