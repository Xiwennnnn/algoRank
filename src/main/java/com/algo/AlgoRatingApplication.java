package com.algo;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;



@SpringBootApplication
@EnableCaching
public class AlgoRatingApplication {

    public static void main(String[] args) {
        SpringApplication.run(AlgoRatingApplication.class, args);
    }

}
