package com.itheima.test;

import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;

public class TestQuartz {

    @Test
    public void test01() throws IOException {

        new ClassPathXmlApplicationContext("applicationContext.xml");

        //读取键盘的输入
        System.in.read();

    }
}
