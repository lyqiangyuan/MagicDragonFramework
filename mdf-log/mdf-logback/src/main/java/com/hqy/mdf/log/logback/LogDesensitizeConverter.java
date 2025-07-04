package com.hqy.mdf.log.logback;

import ch.qos.logback.classic.pattern.MessageConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;
import com.hqy.mdf.log.MdfLogConstant;
import com.hqy.mdf.log.LogDesensitizeProperties;
import com.hqy.mdf.log.LogDesensitizer;
import com.hqy.mdf.log.MdfLogContext;

/**
 * @author hqy
 */
public class LogDesensitizeConverter extends MessageConverter {

    @Override
    public String convert(ILoggingEvent event) {
        // LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
        Object config = MdfLogContext.getObject(MdfLogConstant.LOG_DESENSITIZE_CONFIG_KEY);
        if (config instanceof LogDesensitizeProperties) {
            LogDesensitizeProperties configProperties = (LogDesensitizeProperties) config;
            if (configProperties.isEnable()) {
                return new LogDesensitizer(configProperties).process(event.getFormattedMessage());
            }
        }
        return event.getFormattedMessage();


    }
}