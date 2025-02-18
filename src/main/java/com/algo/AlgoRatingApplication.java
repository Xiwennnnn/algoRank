package com.algo;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cache.annotation.EnableCaching;



@SpringBootApplication
@EnableCaching
public class AlgoRatingApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(AlgoRatingApplication.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(AlgoRatingApplication.class);
    }
}
