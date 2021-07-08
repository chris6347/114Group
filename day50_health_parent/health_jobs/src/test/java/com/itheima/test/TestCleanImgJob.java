package com.itheima.test;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;

public class TestCleanImgJob {

    @Test
    public void testCleanImg() throws IOException {

        new ClassPathXmlApplicationContext("applicationContext-job.xml");
        System.in.read();
    }
}
