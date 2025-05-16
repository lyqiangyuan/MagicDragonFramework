package com.hqy.mdf.dubbo.starter.processor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.boot.env.YamlPropertySourceLoader;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.util.List;

/**
 * @author hqy
 */
@Slf4j
public class DubboBaseEnvironmentPostProcessor implements EnvironmentPostProcessor {

    public static final String FILE_PATH = "mdf-dubbo.yaml";
    public static final String CONFIG_NAME = "mdf-dubbo";

    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {


        Resource resource = new ClassPathResource(FILE_PATH);
            if (resource.exists()) {
                try {
                    List<PropertySource<?>> propertySources = new YamlPropertySourceLoader().load(CONFIG_NAME, resource);
                    if (CollectionUtils.isEmpty(propertySources)) {
                        return;
                    }
                    for (PropertySource<?> propertySource : propertySources) {
                        environment.getPropertySources().addLast(propertySource);
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }


    }
}
