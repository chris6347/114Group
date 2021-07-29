package com.tanhua.server.test;

import com.tanhua.commons.templates.FaceTemplate;
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
public class FaceTest {

    @Autowired
    private FaceTemplate faceTemplate;

    @Test
    public void test() throws IOException {
        byte[] bytes = Files.readAllBytes(new File("/Users/chris/files/1.jpg").toPath());
        System.out.println(faceTemplate.detect(bytes));
    }

}
