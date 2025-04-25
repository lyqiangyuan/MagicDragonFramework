package com.hqy.mdf.common.annotation.security;

import com.fasterxml.jackson.annotation.JacksonAnnotationsInside;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.hqy.mdf.common.annotation.security.serializer.DesensitizationSerializer;
import com.hqy.mdf.common.enums.DesensitizationTypeEnum;

import java.lang.annotation.*;

/**
 * @author hqy
 */
@Documented
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@JacksonAnnotationsInside
@JsonSerialize(using = DesensitizationSerializer.class)
public @interface Desensitization {

    /**
     * 脱敏类型
     */
    DesensitizationTypeEnum type() default DesensitizationTypeEnum.CUSTOMIZE;

    /**
     * 前置无需打码的长度
     */
    int preLength() default 0;

    /**
     * 后置无需打码的长度
     */
    int sufLength() default 0;

    /**
     * 掩码字符
     */
    char markChar() default '*';

}
