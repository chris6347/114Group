package com.tanhua.server.test;

import com.tanhua.commons.templates.FaceTemplate;
import com.tanhua.commons.templates.SmsTemplate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

@SpringBootTest
@RunWith(SpringRunner.class)
public class TestSendSms {

    @Autowired
    private SmsTemplate smsTemplate;

    @Autowired
    private FaceTemplate faceTemplate;

    @Test
    public void test(){
        smsTemplate.sendValidateCode("13316915805","123456");
    }

    @Test
    public void test2() throws IOException {
        boolean detect = faceTemplate.detect(Files.readAllBytes(new File("/Users/chris/files/3.jpeg").toPath()));
        System.out.println(detect);
    }

}
