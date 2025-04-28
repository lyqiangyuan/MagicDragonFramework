package com.hqy.mdf.test.provider;

import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * @author hqy
 */
@Slf4j
@EnableDubbo
@SpringBootApplication
public class ProviderApplication {

    public static void main( String[] args ){
        ConfigurableApplicationContext applicationContext = SpringApplication.run(ProviderApplication.class, args);

        String property = applicationContext.getEnvironment().getProperty("dubbo.provider.validation");
        log.info("property: {}", property);
    }
}
