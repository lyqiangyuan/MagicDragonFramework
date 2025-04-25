package com.hqy.mdf.common.util;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Arrays;

/**
 * 脱敏工具类
 * @author hqy
 */
public class DesensitizationUtils {

    private static final char MASK_CHAR = '*';

    private static final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * 字符串脱敏
     *
     * @param original  原始字符串
     * @param prefixLen 原始字符串中前置无需脱敏的字符长度
     * @param suffixLen 原始字符串中后置无需脱敏的字符长度
     * @param maskChar 掩码字符
     * @return 脱敏后的字符串
     */
    public static String desensitization(String original, int prefixLen, int suffixLen,char maskChar) {
        if (original == null || original.isEmpty()) {
            return original;
        }
        int num = original.length() - prefixLen - suffixLen;
        if (num < 1) {
            return original;
        }
        StringBuilder sbStr = new StringBuilder(original);
        sbStr.replace(prefixLen, original.length() - suffixLen, getMaskStr(num,maskChar));
        return sbStr.toString();
    }

    private static String getMaskStr(int num,char maskChar) {
        char[] chars = new char[num];
        Arrays.fill(chars, maskChar);
        return String.valueOf(chars);
    }

    /**
     * 姓名脱敏。明文为姓名的最后1个字，其余字符都脱敏。如*明
     */
    public static String name(String fullName) {
        return desensitization(fullName, 0, 1, MASK_CHAR);
    }

    /**
     * 手机号码脱敏。前3位和后4位为明文，其余字符脱敏。如186*****9166
     */
    public static String mobileNo(String no) {
        return desensitization(no, 3, 4, MASK_CHAR);
    }

    /**
     * 身份证号码脱敏。前6位和后4位明文，其余字符脱敏。如362329********1010
     */
    public static String idCardNo(String no) {
        return desensitization(no, 6, 4, MASK_CHAR);
    }

    /**
     * 地址脱敏。前6位明文，其余字符脱敏。如上海市徐汇区*****
     */
    public static String address(String addr) {
        return desensitization(addr, 6, 0, MASK_CHAR);
    }

    /**
     * 银行卡号脱敏。前4位和后4位明文，其余字符脱敏。如6227********1010
     */
    public static String bankCardNo(String no) {
        return desensitization(no, 4, 4, MASK_CHAR);
    }


    public static String toJsonStr(Object object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }


}
