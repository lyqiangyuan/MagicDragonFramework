package com.hqy.mdf.test.vo;

import com.hqy.mdf.common.annotation.validation.CheckEnum;
import com.hqy.mdf.common.enums.StatusEnum;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * @author hqy
 */
@Data
public class TestVO implements Serializable {

    @CheckEnum(enumClass = StatusEnum.class, message = "状态不正确")
    private Integer type;

    @NotBlank(message = "姓名不能为空")
    private String name;
}
