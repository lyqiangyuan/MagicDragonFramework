package com.hqy.mdf.dubbo.starter.filter;


import com.hqy.mdf.log.LogDubboProperties;
import com.hqy.mdf.log.MdfLogConstant;
import com.hqy.mdf.log.MdfLogContext;
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
        Object object = MdfLogContext.getObject(MdfLogConstant.LOG_WEB_CONFIG_KEY);
        if (object instanceof LogDubboProperties && ((LogDubboProperties) object).isEnable()) {
            String side = RpcContext.getContext().isProviderSide() ? "provider" : "consumer";
            log.info("dubbo[{}] {}.{} req:{}", side, invoker.getInterface().getName(), invocation.getMethodName(), invocation.getArguments());
            Result result = invoker.invoke(invocation);
            log.info("dubbo[{}] {}.{} resp:{}", side, invoker.getInterface().getName(), invocation.getMethodName(), result.getValue());
            return result;
        }else {
            return invoker.invoke(invocation);
        }
    }

}
