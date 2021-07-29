package com.tanhua.server.test;

import com.tanhua.commons.templates.SmsTemplate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Map;

@SpringBootTest
@RunWith(SpringRunner.class)
public class DuanXinTest {

    @Autowired
    private SmsTemplate smsTemplate;

    @Test
    public void test1(){
        Map<String,String> stringStringMap = smsTemplate.sendValidateCode("13316915805","666666");
        System.out.println(stringStringMap);
    }

    @Test
    public void test2(){
        System.out.println("eyJhbGciOiJIUzI1NiJ9.eyJtb2JpbGUiOiIxMzMxNjkxNTgwNSIsImlkIjoxLCJpYXQiOjE2MjYwOTQ3MDd9.Ghy65nHfYzlvNfTeW4a508VDTh5OM94egYSJZRgup0s".equals("eyJhbGciOiJIUzI1NiJ9.eyJtb2JpbGUiOiIxMzMxNjkxNTgwNSIsImlkIjoxLCJpYXQiOjE2MjYwOTQ3MDd9.Ghy65nHfYzlvNfTeW4a508VDTh5OM94egYSJZRgup0s"));
        System.out.println("eyJhbGciOiJIUzI1NiJ9.eyJtb2JpbGUiOiIxMzMxNjkxNTgwNSIsImlkIjoxLCJpYXQiOjE2MjYwOTQ3MDd9.Ghy65nHfYzlvNfTeW4a508VDTh5OM94egYSJZRgup0s".equals("eyJhbGciOiJIUzI1NiJ9.eyJtb2JpbGUiOiIxMzMxNjkxNTgwNSIsImlkIjoxLCJpYXQiOjE2MjYwOTQ3MDd9.Ghy65nHfYzlvNfTeW4a508VDTh5OM94egYSJZRgup0s"));
    }

}
