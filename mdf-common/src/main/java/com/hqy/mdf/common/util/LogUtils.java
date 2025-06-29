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
        return System.currentTimeMillis() + UUID.randomUUID().toString().replace("-","");
    }

    /**
     * 环境中设置追踪日志Id
     */
    public static void setTraceId(String traceId){
        MDC.put(BaseConst.MDF_TRACE_ID, traceId == null || traceId.isEmpty() ? createTraceId() : traceId);
    }
    /**
     * 从环境中追踪日志Id
     */
    public static String getTraceId(){
        return MDC.get(BaseConst.MDF_TRACE_ID);
    }

    public static void removeTraceId() {
        MDC.remove(BaseConst.MDF_TRACE_ID);
    }



}
