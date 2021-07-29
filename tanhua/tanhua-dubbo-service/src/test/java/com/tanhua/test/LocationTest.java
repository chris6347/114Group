package com.tanhua.test;

import com.tanhua.dubbo.DubboServiceApplication;
import com.tanhua.dubbo.api.UserLocationApi;
import org.apache.commons.lang3.RandomUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest(classes = DubboServiceApplication.class)
@RunWith(SpringRunner.class)
public class LocationTest {

    @Autowired
    private UserLocationApi userLocationApi;

    @Test
    public void testAdd(){
        for (long i = 1; i <= 100; i++) {
            userLocationApi.addLocation(RandomUtils.nextDouble(0.1,179.9),
                    RandomUtils.nextDouble(0.1,179.9),
                    "兴东"+i, i);
        }
    }

}
