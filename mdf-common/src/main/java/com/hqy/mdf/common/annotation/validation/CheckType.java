package com.hqy.mdf.common.annotation.validation;


import com.hqy.mdf.common.annotation.validation.validator.TypeValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * @author hqy
 */
@Documented
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {TypeValidator.class})
public @interface CheckType {

    /**
     * int类型的types
     */
    int[] value() default {} ;

    /**
     * 字符串类型的types
     */
    String[] strValue() default {} ;

    String message() default "type error";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };

}
