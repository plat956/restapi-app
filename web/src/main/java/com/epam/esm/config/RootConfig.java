package com.epam.esm.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import static org.springframework.beans.factory.config.BeanDefinition.SCOPE_PROTOTYPE;

@Configuration
public class RootConfig {

    @Scope(SCOPE_PROTOTYPE)
    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }
}
