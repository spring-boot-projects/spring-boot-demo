package com.xkcoding.cache.redis;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
public class SpringBootDemoCacheRedisApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootDemoCacheRedisApplication.class, args);
    }
}
