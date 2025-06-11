package com.hqy.mdf.test.consumer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author hqy
 */
@Slf4j
@SpringBootApplication
public class TestConsumerApplication {
    public static void main( String[] args ){
        SpringApplication.run(TestConsumerApplication.class, args);
    }
}
