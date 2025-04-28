package com.hqy.mdf.test.api.dto.resp;

import com.hqy.mdf.common.annotation.security.Desensitization;
import com.hqy.mdf.common.enums.DesensitizationTypeEnum;
import lombok.Data;

import java.io.Serializable;

/**
 * @author hqy
 * @date 2025/4/27
 */
@Data
public class TestRespDTO implements Serializable {

    @Desensitization(type = DesensitizationTypeEnum.NAME)
    private String name;
    @Desensitization(type = DesensitizationTypeEnum.MOBILE_NO)
    private String mobileNo;
    @Desensitization(type = DesensitizationTypeEnum.PASSWORD)
    private String password;
    @Desensitization(type = DesensitizationTypeEnum.ID_CARD_NO)
    private String idCardNo;
    @Desensitization(type = DesensitizationTypeEnum.BANK_CARD_NO)
    private String bankCard;
    @Desensitization(type = DesensitizationTypeEnum.ADDRESS)
    private String address;
    @Desensitization(preLength = 2,sufLength = 1)
    private String customize;
}
