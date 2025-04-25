package com.hqy.mdf.test.vo;

import com.hqy.mdf.common.annotation.validation.CheckEnum;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * @author hqy
 */
@Data
public class TestVO implements Serializable {

    @CheckEnum(message = "状态不正确")
    private String type;

    @NotBlank(message = "姓名不能为空")
    private String name;
}
