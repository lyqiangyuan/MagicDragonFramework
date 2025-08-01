package com.hqy.mdf.common.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * @author hqy
 */
public enum EncryptTypeEnum {
    //对称加密
    SYMMETRY_DES(101,"DES","DES",EncryptGroupEnum.SYMMETRY),
    SYMMETRY_3DES(102,"3DES","DESEDE",EncryptGroupEnum.SYMMETRY),
    SYMMETRY_AES(103,"AES","AES",EncryptGroupEnum.SYMMETRY),
    SYMMETRY_SM4(104,"SM4","SM4",EncryptGroupEnum.SYMMETRY),
    //非对加密
    ASYMMETRY_RSA(201,"RSA","RSA",EncryptGroupEnum.ASYMMETRY),
    ASYMMETRY_SM2(202,"SM2","EC",EncryptGroupEnum.ASYMMETRY),
    //摘要加密
    DIGEST_MD5(301,"MD5","MD5",EncryptGroupEnum.DIGEST),
    DIGEST_SHA1(302,"SHA1","SHA",EncryptGroupEnum.DIGEST),
    DIGEST_SHA256(303,"SHA256","SHA-256",EncryptGroupEnum.DIGEST),
    DIGEST_SM3(304,"SM3","SM3",EncryptGroupEnum.DIGEST),
    ;


    private Integer type;
    private String name;
    private String algorithm;
    private EncryptGroupEnum encryptGroupEnum;

    EncryptTypeEnum(Integer type, String name, String algorithm, EncryptGroupEnum encryptGroupEnum) {
        this.type = type;
        this.name = name;
        this.algorithm = algorithm;
        this.encryptGroupEnum = encryptGroupEnum;
    }

    public static final Map<Integer, EncryptTypeEnum> ENUM_MAP = new HashMap();
    public static final Map<String, EncryptTypeEnum> ALGORITHM_ENUM_MAP = new HashMap();

    static {
        for (EncryptTypeEnum value : EncryptTypeEnum.values()) {
            ENUM_MAP.put(value.getType(),value);
            ALGORITHM_ENUM_MAP.put(value.getAlgorithm(),value);
        }
    }

    public static EncryptTypeEnum getEnumByType(Integer type){
        return ENUM_MAP.get(type);
    }

    public static EncryptTypeEnum getEnumByAlgorithm(String algorithm){
        return ALGORITHM_ENUM_MAP.get(algorithm);
    }

    public Integer getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public String getAlgorithm() {
        return algorithm;
    }

    public EncryptGroupEnum getEncryptGroupEnum() {
        return encryptGroupEnum;
    }
}
