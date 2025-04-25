package com.hqy.mdf.common.bean;

/**
 * @author hqy
 */
public interface GainEnum<R extends Enum<?>> {

    R getEnumByCode(Object code);
}
