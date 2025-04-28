package com.hqy.mdf.base.starter.filter;


import com.hqy.mdf.common.constant.BaseConst;
import com.hqy.mdf.common.util.LogUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.common.constants.CommonConstants;
import org.apache.dubbo.common.extension.Activate;
import org.apache.dubbo.common.utils.StringUtils;
import org.apache.dubbo.rpc.*;
import org.slf4j.MDC;

/**
 * dubbo接口异常处理过滤器
 * @author hqy
 */
@Slf4j
@Activate(group = {CommonConstants.PROVIDER,CommonConstants.CONSUMER})
public class DubboTraceIdExceptionFilter implements Filter {

    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        //客户端逻辑处理
        if (RpcContext.getContext().isConsumerSide()) {
            String traceId = LogUtils.getTraceId();
            if (StringUtils.isEmpty(traceId)) {
                traceId = LogUtils.createTraceId();
                LogUtils.setTraceId(traceId);
            }
            RpcContext.getContext().setAttachment(BaseConst.TRACE_ID, traceId);
        } else {
            //在从上下文中获取
            String traceId = RpcContext.getContext().getAttachment(BaseConst.TRACE_ID);
            if (StringUtils.isEmpty(traceId)) {
                traceId = LogUtils.createTraceId();
            }
            //设置追踪日志ID
            MDC.put(BaseConst.TRACE_ID, traceId);
        }
        return invoker.invoke(invocation);
    }

}
