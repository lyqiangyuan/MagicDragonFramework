package com.hqy.mdf.common.annotation.validation.validator;


import com.hqy.mdf.common.annotation.validation.CheckType;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * @author hqy
 */
public class TypeValidator implements ConstraintValidator<CheckType, Object> {

    private final Set<String> typeSet = new HashSet<>();


    @Override
    public void initialize(CheckType constraintAnnotation) {
        for (int value : constraintAnnotation.value()) {
            typeSet.add(String.valueOf(value));
        }
        typeSet.addAll(Arrays.asList(constraintAnnotation.strValue()));

    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {

        if (value == null) {
            return true;
        }

        if (value instanceof String) {
            return typeSet.contains(value);
        }

        if (value instanceof Integer) {
            return typeSet.contains(value.toString());
        }
        return false;

    }
}
