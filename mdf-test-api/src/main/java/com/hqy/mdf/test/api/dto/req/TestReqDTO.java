package com.hqy.mdf.test.api.dto.req;

import com.hqy.mdf.common.annotation.validation.CheckEnum;
import com.hqy.mdf.common.enums.StatusEnum;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * @author hqy
 * @date 2025/4/27
 */
@Data
public class TestReqDTO implements Serializable {

    @NotBlank(message = "名称不能为空")
    private String name;

    @CheckEnum(enumClass = StatusEnum.class,message = "状态不正确")
    private Integer status;
}
