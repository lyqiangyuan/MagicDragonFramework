package com.hqy.mdf.log.logback;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.pattern.MessageConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;
import org.slf4j.LoggerFactory;

/**
 * @author hqy
 */
public class LogDesensitizingConverter extends MessageConverter {

    @Override
    public String convert(ILoggingEvent event) {
        LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
        Object config = loggerContext.getObject("mdf.log.desensitize.config");
        if (config instanceof LogDesensitizeProperties) {
            LogDesensitizeProperties configProperties = (LogDesensitizeProperties) config;
            if (configProperties.isOpen()) {
                return new LogDesensitizer(configProperties).process(event.getFormattedMessage());
            }
        }
        return event.getFormattedMessage();


    }
}