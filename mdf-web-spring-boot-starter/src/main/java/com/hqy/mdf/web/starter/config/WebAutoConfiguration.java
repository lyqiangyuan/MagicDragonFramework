package com.hqy.mdf.web.starter.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author hqy
 */
@Configuration
@ConditionalOnWebApplication
@Import({HandlerConfig.class, FilterConfig.class,InterceptorConfig.class})
public class WebAutoConfiguration {


}
