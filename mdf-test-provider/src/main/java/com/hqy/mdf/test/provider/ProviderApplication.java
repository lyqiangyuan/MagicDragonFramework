package com.hqy.mdf.test.provider;

import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author hqy
 */
@Slf4j
@EnableDubbo
@SpringBootApplication
public class ProviderApplication {

    public static void main( String[] args ){
        SpringApplication.run(ProviderApplication.class, args);
    }
}
