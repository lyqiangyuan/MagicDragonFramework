package com.hqy.mdf.log.logback;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.filter.AbstractMatcherFilter;
import ch.qos.logback.core.spi.FilterReply;

public class CustomFilter extends AbstractMatcherFilter<ILoggingEvent> {
    @Override
    public FilterReply decide(ILoggingEvent event) {
        // 自定义过滤逻辑（例如按用户ID、日志内容过滤）
        if (event.getMessage().contains("sensitive")) {
            return FilterReply.DENY;
        }
        return FilterReply.NEUTRAL;
    }
}