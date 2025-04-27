package com.hqy.mdf.common.bean;

import com.hqy.mdf.common.enums.ErrorEnum;
import com.hqy.mdf.common.util.LogUtils;
import lombok.Data;

import java.io.Serializable;

/**
 * @author hqy
 */
@Data
public class Result<T> implements Serializable {

    /**
     * 响应对象
     */
    private T result;

    /**
     * 错误编码
     */
    private String errorCode;

    /**
     * 错误描述
     */
    private String errorMsg;

    /**
     * 追踪日志ID
     */
    private String traceId;


    public Result() {
        this.traceId = LogUtils.getTraceId();
    }


    public Result(T result) {
        this.result = result;
        this.errorCode = ErrorEnum.SUCCESS.getCode();
        this.errorMsg = ErrorEnum.SUCCESS.getMsg();
        this.traceId = LogUtils.getTraceId();
    }

    public Result(ErrorCodeMsg error) {
        this.errorCode = error.getCode();
        this.errorMsg = error.getMsg();
        this.traceId = LogUtils.getTraceId();
    }

    public Result(String errorCode, String errorMsg) {
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
        this.traceId = LogUtils.getTraceId();
    }

    public boolean isSuccess() {
        return ErrorEnum.SUCCESS.getCode().equals(this.errorCode);
    }
}
