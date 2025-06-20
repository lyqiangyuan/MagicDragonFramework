package com.hqy.mdf.log.config;

import com.hqy.mdf.log.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @author hqy
 */
@Getter
@Setter
@Component
@RefreshScope
@ConfigurationProperties(prefix = "mdf.log")
public class MdfLogProperties {

    private LogWebProperties web;

    private LogDubboProperties dubbo;

    private LogDesensitizeProperties desensitize;



    @PostConstruct
    public void init() {
        if (web == null) {
            MdfLogContext.putObject(MdfLogConstant.LOG_WEB_CONFIG_KEY,web);
        }
        if (dubbo == null) {
            MdfLogContext.putObject(MdfLogConstant.LOG_DUBBO_CONFIG_KEY,dubbo);
        }
        if (desensitize == null) {
            MdfLogContext.putObject(MdfLogConstant.LOG_DESENSITIZE_CONFIG_KEY,desensitize);
        }
    }
}
