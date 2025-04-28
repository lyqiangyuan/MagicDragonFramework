package com.hqy.mdf.common.exception;


import com.hqy.mdf.common.bean.ErrorCodeMsg;

/**
 * @author hqy
 */
public class BaseException extends RuntimeException implements ErrorCodeMsg{

    /**
     * 错误码
     */
    private final String errorCode;

    /**
     * 错误描述
     */
    private final String errorMsg;


    public BaseException(String errorCode, String errorMsg) {
        super(errorMsg);
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }

    public BaseException(ErrorCodeMsg errorCodeMsg) {
        this(errorCodeMsg.getCode(), errorCodeMsg.getMsg());
    }

    @Override
    public String getCode() {
        return errorCode;
    }

    @Override
    public String getMsg() {
        return errorMsg;
    }
}
