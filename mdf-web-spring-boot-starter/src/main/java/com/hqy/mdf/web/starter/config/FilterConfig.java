package com.hqy.mdf.web.starter.config;

import com.hqy.mdf.web.starter.filter.WebLogFilter;
import com.hqy.mdf.web.starter.filter.WebTraceIdFilter;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author hqy
 */
@Configuration
@Import({WebTraceIdFilter.class, WebLogFilter.class})
public class FilterConfig {
}
