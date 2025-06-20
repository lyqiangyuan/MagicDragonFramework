package com.hqy.mdf.log.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author hqy
 */

@Configuration
@Import({MdfLogProperties.class})
public class LogAutoConfiguration {
}
