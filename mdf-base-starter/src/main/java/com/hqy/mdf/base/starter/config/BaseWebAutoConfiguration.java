package com.hqy.mdf.base.starter.config;

import com.hqy.mdf.base.starter.handler.WebGlobalExceptionHandler;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author hqy
 */
@Configuration
@ConditionalOnWebApplication
@Import({WebGlobalExceptionHandler.class})
public class BaseWebAutoConfiguration {

}
