package com.itheima;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.EnableScheduling;

// 是一个springboot项目,这个类就是启动类 暂时这样理解
@SpringBootApplication
//@PropertySource("classpath:application.yml")
@MapperScan(basePackages = "com.itheima.dao")
@EnableScheduling  // 开启定时任务
public class QuickStartApplication {

    public static void main(String[] args) {
        // p1:启动类字节码  p2:启动时额外参数,通常没有传值
        // 返回值为spring容器ApplicationContext
        SpringApplication.run(QuickStartApplication.class, args);
    }

}
