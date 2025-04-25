package com.hqy.mdf.common.annotation.validation;


import com.hqy.mdf.common.annotation.validation.validator.EnumValidator;
import com.hqy.mdf.common.bean.GainEnum;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @author hqy
 */
@Target({FIELD, PARAMETER})
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = {EnumValidator.class})
public @interface CheckEnum {

    Class<? extends GainEnum> enumClass() default GainEnum.class;

    String message() default "类型不正确";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
