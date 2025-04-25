package com.hqy.mdf.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author hqy
 */
@Getter
@AllArgsConstructor
public enum DesensitizationTypeEnum {


    /**
     * 默认
     */
    DEFAULT("DEFAULT",1,1,'*'),
    /**
     * 自定义
     */
    CUSTOMIZE("CUSTOMIZE",0,0,'*'),

    /**
     * 密码
     */
    PASSWORD("PASSWORD",0,0,'*'),

    /**
     * 姓名
     */
    NAME("NAME",0,1,'*'),
    /**
     * 手机号码
     */
    MOBILE_NO("MOBILE_NO",3,4,'*'),
    /**
     * 身份证号码
     */
    ID_CARD_NO("ID_CARD_NO",6,4,'*'),
    /**
     * 银行卡号
     */
    BANK_CARD_NO("BANK_CARD_NO",4,4,'*'),
    /**
     * 地址
     */
    ADDRESS("ADDRESS",6,0,'*'),
    ;

    private final String type;

    /**
     * 原始字符串中前置无需脱敏的字符长度
     */
    private final int preLength;
    /**
     * 原始字符串中后置无需脱敏的字符长度
     */
    private final int sufLength;

    /**
     * 掩码字符
     */
    private final char markChar;
}
