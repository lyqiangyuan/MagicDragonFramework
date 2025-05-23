package com.hqy.mdf.web.starter.config;

import com.hqy.mdf.web.starter.handler.WebGlobalExceptionHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author hqy
 */
@Configuration
@Import({WebGlobalExceptionHandler.class})
public class HandlerConfig {
}
