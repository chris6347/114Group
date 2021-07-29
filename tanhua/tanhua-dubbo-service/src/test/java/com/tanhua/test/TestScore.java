package com.tanhua.test;

import com.tanhua.domain.db.RecommendUser;
import com.tanhua.dubbo.DubboServiceApplication;
import com.tanhua.dubbo.api.FriendApi;
import com.tanhua.dubbo.api.RecommendUserApi;
import org.apache.commons.lang3.RandomUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.SimpleDateFormat;
import java.util.Date;

@SpringBootTest(classes = DubboServiceApplication.class)
@RunWith(SpringRunner.class)
public class TestScore {

    @Autowired
    private RecommendUserApi recommendUserApi;

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    @Test
    public void test(){
        RecommendUser recommendUser = new RecommendUser();
        for (long i = 4; i <= 10000; i++) {
            recommendUser.setId(null);
            recommendUser.setToUserId(1L);
            recommendUser.setUserId(i);
            recommendUser.setScore(RandomUtils.nextDouble(50,100));
            recommendUser.setDate(sdf.format(new Date(RandomUtils.nextInt(16264147,162677414))));
            recommendUserApi.save(recommendUser);
        }
    }

}
