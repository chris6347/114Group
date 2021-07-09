package com.itheima.test;

import org.junit.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class TestPasswordEncoder {

    @Test
    public void test01(){

        //1. 创建对象
        BCryptPasswordEncoder pe = new BCryptPasswordEncoder();

        /*
            2. 加密 : 注册
                $2a$10$MSJ5VBvGvzBTDJyjX33saekTrDta20CtcAdg7uhS9zaMNzkQUSXTK
                $2a$10$8ubHliIYO3bdORFnpsh95e3q7Tqd5GQr.8uzPE2ubyJAiUNtsyivi
         */

        System.out.println(pe.encode("1234"));
        System.out.println(pe.encode("1234"));

        //3. 匹配： 登录
        boolean flag = pe.matches("1234", "$2a$10$kJnP/fhz1HLyYd6gJ3IU1eBUHo3IuUIavErfeYWIRU7B9JCtnkdZa");
        System.out.println("flag=" + flag);

    }
}
