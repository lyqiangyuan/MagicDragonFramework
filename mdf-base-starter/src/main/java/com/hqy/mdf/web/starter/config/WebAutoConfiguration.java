package com.hqy.mdf.web.starter.config;

import com.hqy.mdf.web.starter.filter.WebTraceIdFilter;
import com.hqy.mdf.web.starter.handler.WebGlobalExceptionHandler;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author hqy
 */
@Configuration
@ConditionalOnWebApplication
@Import({WebGlobalExceptionHandler.class, WebTraceIdFilter.class})
public class WebAutoConfiguration {

}
