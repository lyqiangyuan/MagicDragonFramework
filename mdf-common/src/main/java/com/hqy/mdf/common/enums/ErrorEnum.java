package com.hqy.mdf.common.enums;

import com.hqy.mdf.common.bean.ErrorCodeMsg;
import com.hqy.mdf.common.bean.GainEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

/**
 * 定义错误码 8位
 * 格式 业务编号（2）-应用编号（2位）-问题域（1位）-错误编码（4位）
 * 业务编号:10-99  10留给架构 11-99留给业务 00-09保留
 * 应用编号:00-99  00留给通用 01-99留给业务
 * 问题域：0-9 0:系统域  1：参数域  2：业务域 3：数据域 4:下游域 5:三方域 9：未知
 * 错误编码：0000-9999
 *
 * @author hqy
 */
@Getter
@AllArgsConstructor
public enum ErrorEnum implements ErrorCodeMsg, GainEnum<ErrorEnum> {

    //通用
    SUCCESS("0","成功"),

    //架构层
    UNKNOWN_ERROR("100000000","系统繁忙，请稍后重试"),
    REQUEST_PATH_ERROR("100000001","请求地址错误"),
    REQUEST_METHODS_NOT_SUPPORTED("100000002","请求方法不支持"),
    PARAM_ERROR("100000003","参数错误"),
    PARAM_FORMAT_ERROR("1000004","参数格式不正确"),
    INTERNAL_HAPPEN_EXCEPTION("1000005","内部异常"),
    TIMEOUT_ERROR("1000006","调用超时"),
    ;

    private final String code;
    private final String msg;

    public static final Map<String, ErrorEnum> ENUM_MAP = new HashMap<>();
    static {
        for (ErrorEnum value : ErrorEnum.values()) {
            ENUM_MAP.put(value.code,value);
        }
    }

    @Override
    public String getCode() {
        return this.code;
    }

    @Override
    public String getMsg() {
        return this.msg;
    }

    @Override
    public ErrorEnum getEnumByCode(Object code) {
        if (code instanceof String) {
            return ENUM_MAP.get((String) code);
        }
        return null;
    }


}
