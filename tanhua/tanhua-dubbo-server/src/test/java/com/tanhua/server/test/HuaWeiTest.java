package com.tanhua.server.test;

import com.tanhua.commons.templates.HuaWeiUGCTemplate;
import com.tanhua.server.TanhuaServerApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest(classes = TanhuaServerApplication.class)
@RunWith(SpringRunner.class)
public class HuaWeiTest {

    @Autowired
    private HuaWeiUGCTemplate huaWeiUGCTemplate;

    @Test
    public void testToken(){
        System.out.println(huaWeiUGCTemplate.getToken());
    }

    @Test
    public void testText(){
        boolean check = huaWeiUGCTemplate.textContentCheck("操,fuck");
        System.out.println(check);
    }

    @Test
    public void testImages(){
        String[] urls = new String[]{
                "http://tanhua-group1.oss-cn-shenzhen.aliyuncs.com/images/2021/07/14/3016573b-c0a8-4321-ad42-9f18a95c6106.jpg"
        };
        boolean check = huaWeiUGCTemplate.imageContentCheck(urls);
        System.out.println(check);
    }

}
