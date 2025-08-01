package com.hqy.mdf.common.enums;

import com.hqy.mdf.common.bean.ErrorCodeMsg;
import com.hqy.mdf.common.bean.GainEnum;

import java.util.HashMap;
import java.util.Map;

/**
 * @author hqy
 */
public enum EncryptionErrorEnum implements ErrorCodeMsg, GainEnum<EncryptionErrorEnum> {

    //基本码
    SUCCESS("0","成功"),
    SYSTEM_ERROR("-1","系统错误"),

    //业务入参错误码
    ACCOUNT_ERROR("601001","账号或密码错误!"),
    ACCOUNT_NOT_EXIST("601002","用户不存在!"),
    ACCOUNT_NO_PERMISSION("601003","没有权限!"),
    BIZ_SYSTEM_CODE_NULL("601004","bizSystemCode 不能为空!"),
    NOT_SUPPORT_ENCRYPT_TYPE("601005","加密类型暂不支持!"),
    ENCRYPT_DATA_NULL("601006","encryptData 不能为空!"),
    DENCRYPT_DATA_NULL("601007","dEncryptData 不能为空!"),
    ENCRYPT_DATA_LIST_NULL("601008","encryptDataList 不能为空!"),
    DENCRYPT_DATA_LIST_NULL("601009","dEncryptDataList 不能为空!"),
    ENCRYPT_DATA_LIST_EXIST_NULL("601010","encryptDataList 存在空值!"),
    DENCRYPT_DATA_LIST_EXIST_NULL("601011","dEncryptDataList 存在空值!"),
    ENCRYPT_DATA_RSA_LIMIT_117("601012","RSA算法加密数据不能超过117字节!"),
    DENCRYPT_DATA_RSA_LIMIT_256("601013","RSA算法解密数据不能超过256字节!"),

    KEY_GENERATE_ERROR("602001","生成秘钥失败!"),
    ENCRYPT_ERROR("602002","加密出错了!"),
    DENCRYPT_ERROR("602003","解密出错了!"),
    SECRET_KEY_NULL("602004","秘钥不存在!"),
    SECRET_KEY_NOT_OPEN("602005","秘钥未启用!"),
    NOT_SUPPORT_DENCRYPT("602006","摘要算法不支持解密!"),
    BIZ_SYSTEM_CODE_HAVE_EXISTED("602007","bizSystemCode 已经存在!"),
    BIZ_SYSTEM_CODE_ENCRYPT_TYPE_ERROR("602008","bizSystemCode和encryptType不匹配!"),
    INIT_CHECK_ENCRYPT_ERROR("602009","初始化检查算法错误!"),
    ;

    private String code;
    private String desc;

    EncryptionErrorEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static final Map<String, EncryptionErrorEnum> ENUM_MAP = new HashMap();

    static {
        for (EncryptionErrorEnum value : EncryptionErrorEnum.values()) {
            ENUM_MAP.put(value.getCode(),value);
        }
    }

    public String getCode() {
        return code;
    }

    @Override
    public String getMsg() {
        return desc;
    }

    public String getDesc() {
        return desc;
    }

    @Override
    public EncryptionErrorEnum getEnumByCode(Object code) {
        if (code instanceof String) {
            return ENUM_MAP.get((String) code);
        }
        return null;
    }
}
