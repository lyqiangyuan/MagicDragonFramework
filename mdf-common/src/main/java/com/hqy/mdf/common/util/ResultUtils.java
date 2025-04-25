package com.hqy.mdf.common.util;


import com.hqy.mdf.common.bean.ErrorCodeMsg;
import com.hqy.mdf.common.bean.Result;

/**
 * @author hqy
 */
public class ResultUtils {
    /**
     * 成功
     */
    public static <T> Result<T> success(T data){
        return new Result<T>(data);
    }
    /**
     * 失败
     */
    public static <T> Result<T> error(ErrorCodeMsg errorCodeMsg){
        return new Result<T>(errorCodeMsg);
    }

    /**
     * 失败
     */
    public static <T> Result<T> error(String code, String message) {
        return new Result<T>(code,message);
    }
}
