package com.hqy.mdf.log.logback;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.PatternLayout;
import ch.qos.logback.classic.spi.Configurator;
import ch.qos.logback.core.spi.ContextAwareBase;

public class SensitiveConverterConfigurator extends ContextAwareBase implements Configurator {

    @Override
    public void configure(LoggerContext loggerContext) {

        // 在应用启动最早阶段注册 Converter
        PatternLayout.defaultConverterMap.put("m", LogDesensitizingConverter.class.getName());
        PatternLayout.defaultConverterMap.put("msg", LogDesensitizingConverter.class.getName());
        PatternLayout.defaultConverterMap.put("message", LogDesensitizingConverter.class.getName());

    }
}