package com.itheima;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
public class MybatisPlusConsumerApplication {

    public static void main(String[] args) {
        SpringApplication.run(MybatisPlusConsumerApplication.class,args);
    }

}
