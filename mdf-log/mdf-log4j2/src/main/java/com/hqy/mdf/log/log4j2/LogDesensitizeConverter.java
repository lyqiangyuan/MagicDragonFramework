package com.hqy.mdf.log.log4j2;

import com.hqy.mdf.log.rule.LogDesensitizeConfig;
import com.hqy.mdf.log.rule.LogDesensitizeProperties;
import com.hqy.mdf.log.rule.LogDesensitizer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.pattern.ConverterKeys;
import org.apache.logging.log4j.core.pattern.LogEventPatternConverter;
import org.apache.logging.log4j.core.pattern.MessagePatternConverter;
import org.apache.logging.log4j.core.pattern.PatternConverter;
import org.apache.logging.log4j.spi.LoggerContext;
import org.apache.logging.log4j.util.PerformanceSensitive;

/**
 * @author hqy
 */
@Plugin(name = "LogDesensitizeConverter", category = PatternConverter.CATEGORY)
@ConverterKeys({ "m", "msg", "message" })
@PerformanceSensitive("allocation")
public class LogDesensitizeConverter extends LogEventPatternConverter {

    private final MessagePatternConverter messagePatternConverter;

    private LogDesensitizeConverter(final Configuration config, final String[] options) {
        super("Message", "message");
        this.messagePatternConverter = MessagePatternConverter.newInstance(config, options);
    }

    public static LogDesensitizeConverter newInstance(final Configuration config, final String[] options) {
        return new LogDesensitizeConverter(config, options);
    }


    @Override
    public void format(LogEvent event, StringBuilder toAppendTo) {
        LoggerContext loggerContext = LogManager.getContext(false);
        if (loggerContext != null) {
            Object config = loggerContext.getObject(LogDesensitizeConfig.LOG_DESENSITIZE_CONFIG);
            if (config instanceof LogDesensitizeProperties) {
                LogDesensitizeProperties configProperties = (LogDesensitizeProperties) config;
                if (configProperties.isOpen()) {
                    StringBuilder originalMessage = new StringBuilder();
                    messagePatternConverter.format(event, originalMessage);
                    String desensitizeLog = new LogDesensitizer(configProperties).process(originalMessage.toString());
                    toAppendTo.append(desensitizeLog);
                    return;
                }
            }
        }
        messagePatternConverter.format(event, toAppendTo);
    }
}
