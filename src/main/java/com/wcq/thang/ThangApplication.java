package com.wcq.thang;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.wcq.thang.mapper")
public class ThangApplication {

    public static void main(String[] args) {
        SpringApplication.run(ThangApplication.class, args);
    }

}
