package com.itheima;

import com.baomidou.mybatisplus.autoconfigure.MybatisPlusAutoConfiguration;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(exclude = MybatisPlusAutoConfiguration.class)
@MapperScan("com.itheima.dao")
public class MybatisPlusPraApplication {

    public static void main(String[] args) {
        SpringApplication.run(MybatisPlusPraApplication.class,args);
    }

}
