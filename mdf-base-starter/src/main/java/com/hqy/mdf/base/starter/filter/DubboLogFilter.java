package com.hqy.mdf.base.starter.filter;


import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.common.constants.CommonConstants;
import org.apache.dubbo.common.extension.Activate;
import org.apache.dubbo.rpc.*;

/**
 * dubbo接口异常处理过滤器
 * @author hqy
 */
@Slf4j
@Activate(group = {CommonConstants.PROVIDER,CommonConstants.CONSUMER})
public class DubboLogFilter implements Filter {

    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        String side = RpcContext.getContext().isProviderSide() ? "provider" : "consumer";
        log.info("dubbo[{}] {}.{} req:{}", side, invoker.getInterface().getName(), invocation.getMethodName(), invocation.getArguments());
        Result result = invoker.invoke(invocation);
        log.info("dubbo[{}] {}.{} resp:{}", side, invoker.getInterface().getName(), invocation.getMethodName(), result.getValue());
        return result;
    }

}
