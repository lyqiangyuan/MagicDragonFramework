package com.hqy.mdf.common.bean;

/**
 * @author hqy
* @date 2025/4/23
 */
public interface GainEnum<R extends Enum<?>> {

    R getEnumByCode(Object code);
}
