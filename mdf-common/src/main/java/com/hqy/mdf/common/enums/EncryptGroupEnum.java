package com.hqy.mdf.common.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * @author hqy
 */
public enum EncryptGroupEnum {
    //对称加密
    SYMMETRY(1,"对称加密算法"),
    ASYMMETRY(2,"非对称加密算法"),
    DIGEST(3,"摘要加密算法"),
    ;

    private Integer groupType;
    private String groupName;

    EncryptGroupEnum(Integer groupType, String groupDesc) {
        this.groupType = groupType;
        this.groupName = groupDesc;
    }

    public static final Map<Integer, EncryptGroupEnum> ENUM_MAP = new HashMap();

    static {
        for (EncryptGroupEnum value : EncryptGroupEnum.values()) {
            ENUM_MAP.put(value.getGroupType(),value);
        }
    }

    public static EncryptGroupEnum getEnumByType(Integer groupType){
        return ENUM_MAP.get(groupType);
    }

    public Integer getGroupType() {
        return groupType;
    }

    public String getGroupName() {
        return groupName;
    }
}
