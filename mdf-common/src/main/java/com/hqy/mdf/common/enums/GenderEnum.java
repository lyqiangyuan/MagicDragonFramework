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
public enum GenderEnum implements GainEnum<GenderEnum> {

    UNKNOWN(0,"UNKNOWN","未知"),
    MAN(1,"MALE","男"),
    WOMAN(2,"FEMALE","女"),
    ;

    private final Integer code;

    private final String type;

    private final String desc;

    public static final Map<Object, GenderEnum> ENUM_MAP = new HashMap<>();

    static {
        for (GenderEnum value : GenderEnum.values()) {
            ENUM_MAP.put(value.code,value);
        }
    }

    public static GenderEnum getByCode(Integer code){
        return ENUM_MAP.get(code);
    }

    public static GenderEnum getByType(String type){
        return ENUM_MAP.get(type);
    }

    @Override
    public GenderEnum getEnumByCode(Object code) {
        return ENUM_MAP.get(code);
    }
}
