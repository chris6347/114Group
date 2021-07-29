package com.tanhua.test;

import com.tanhua.domain.mongo.Visitor;
import com.tanhua.dubbo.DubboServiceApplication;
import com.tanhua.dubbo.api.VisitorApi;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest(classes = DubboServiceApplication.class)
@RunWith(SpringRunner.class)
public class TestVisitors {

    @Autowired
    private VisitorApi visitorApi;

    @Test
    public void testAdd(){
        Visitor visitor = null;
        for (int i = 1; i <= 10; i++) {
            visitor = new Visitor();
            visitor.setUserId(1L);
            visitor.setVisitorUserId((long)i);
            visitor.setFrom("首页");
            visitor.setDate(System.currentTimeMillis());
            visitorApi.save(visitor);
        }
        System.out.println("ok");
    }

}
