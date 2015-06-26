package com.proto;

import org.dozer.DozerBeanMapper;
import org.dozer.Mapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class ProtodirectApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProtodirectApplication.class, args);
    }

    @Bean
    public Mapper dozerBeanMapper() {
        return new DozerBeanMapper();
    }

}
