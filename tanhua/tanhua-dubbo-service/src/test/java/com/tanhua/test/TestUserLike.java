package com.tanhua.test;

import com.tanhua.dubbo.DubboServiceApplication;
import com.tanhua.dubbo.api.FriendApi;
import com.tanhua.dubbo.api.UserLikeApi;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest(classes = DubboServiceApplication.class)
@RunWith(SpringRunner.class)
public class TestUserLike {

    @Autowired
    private UserLikeApi userLikeApi;

    @Autowired
    private FriendApi friendApi;

    @Test
    public void test(){
        for (long i = 0; i < 70; i++) {
            userLikeApi.saveLike(1L,i);
        }
        for (long i = 567; i < 10000; i+=3) {
            userLikeApi.saveLike(i,1L);
        }
    }

    @Test
    public void testFriend(){
        for (long i = 169; i < 10000; i+=3) {
            friendApi.addFriend(1L,i);
        }
    }

}
