package com.hqy.mdf.common.enums;

import com.hqy.mdf.common.bean.GainEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

/**
 * @author hqy
 */
@Getter
@AllArgsConstructor
public enum StatusEnum implements GainEnum<StatusEnum> {

    //通用
    INVALID(0,"无效"),
    NORMAL(1,"正常"),
    DELETE(2,"删除"),
    ;

    private final Integer code;
    private final String msg;

    public static final Map<Object, StatusEnum> ENUM_MAP = new HashMap<>();
    static {
        for (StatusEnum value : StatusEnum.values()) {
            ENUM_MAP.put(value.code,value);
        }
    }

    public static StatusEnum getByCode(Integer code){
        return ENUM_MAP.get(code);
    }

    @Override
    public StatusEnum getEnumByCode(Object code) {
        return  ENUM_MAP.get(code);
    }


}
