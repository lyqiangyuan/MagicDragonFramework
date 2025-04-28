package com.hqy.mdf.base.starter.filter;


import com.hqy.mdf.common.enums.ErrorEnum;
import com.hqy.mdf.common.exception.BaseException;
import com.hqy.mdf.common.util.ResultUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.common.constants.CommonConstants;
import org.apache.dubbo.common.extension.Activate;
import org.apache.dubbo.rpc.*;
import org.apache.dubbo.rpc.filter.ExceptionFilter;

import javax.validation.ValidationException;

/**
 * dubbo接口异常处理过滤器
 * @author hqy
 */
@Slf4j
@Activate(group = {CommonConstants.PROVIDER}, order = 1)
public class DubboGlobalExceptionFilter extends ExceptionFilter {

    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {

        Result result = super.invoke(invoker, invocation);
        // 如果有异常，则进行异常处理
        if (result.hasException()) {
            Throwable exception = result.getException();
            //参数校验异常
            if (exception instanceof ValidationException) {
                return AsyncRpcResult.newDefaultAsyncResult(ResultUtils.error(ErrorEnum.PARAM_ERROR.getCode(), exception.getMessage()), invocation);
            }
            //业务异常
            if (exception instanceof BaseException) {
                return AsyncRpcResult.newDefaultAsyncResult(ResultUtils.error((BaseException) exception), invocation);
            }
        }
        return result;

    }

}
