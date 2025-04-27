package com.hqy.mdf.common.util;

import com.hqy.mdf.common.constant.BaseConst;
import org.slf4j.MDC;

import java.util.UUID;

/**
 * @author hqy
 */
public class LogUtils {

    /**
     * 创建追踪Id
     */
    public static String createTraceId(){
        return UUID.randomUUID().toString();
    }

    /**
     * 环境中设置追踪日志Id
     */
    public static void setTraceIdIfAbsent(String traceId){
        MDC.put(BaseConst.TRACE_ID, traceId == null || traceId.isEmpty() ? createTraceId() : traceId);
    }
    /**
     * 从环境中追踪日志Id
     */
    public static String getTraceId(){
        return MDC.get(BaseConst.TRACE_ID);
    }

    /**
     * 从环境中追踪日志Id,如果不存在，创建一个
     */
    public static String getTraceIdIfAbsent() {
        String traceId = getTraceId();
        return traceId == null || traceId.isEmpty() ? createTraceId() : traceId;
    }

    public static void removeTraceId() {
        MDC.remove(BaseConst.TRACE_ID);
    }



}
