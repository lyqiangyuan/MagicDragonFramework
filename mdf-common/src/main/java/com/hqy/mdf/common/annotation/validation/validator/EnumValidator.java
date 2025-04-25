package com.hqy.mdf.common.annotation.validation.validator;


import com.hqy.mdf.common.annotation.validation.CheckEnum;
import com.hqy.mdf.common.bean.GainEnum;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @author hqy
 */
public class EnumValidator implements ConstraintValidator<CheckEnum, Object> {

    private CheckEnum annotation;

    @Override
    public void initialize(CheckEnum constraintAnnotation) {
        annotation = constraintAnnotation;
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext constraintValidatorContext) {
        if (value == null) {
            return true;
        }
        Class<?> aClass = annotation.enumClass();
        Object[] enumConstants = aClass.getEnumConstants();
        if(enumConstants == null || enumConstants.length == 0){
            return false;
        }
        return ((GainEnum<?>) enumConstants[0]).getEnumByCode(value) != null;
    }
}
