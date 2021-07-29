package com.tanhua.test;

import com.alibaba.fastjson.JSON;
import com.tanhua.domain.db.User;
import com.tanhua.dubbo.DubboServiceApplication;
import com.tanhua.dubbo.mapper.UserMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest(classes = DubboServiceApplication.class)
@RunWith(SpringRunner.class)
public class Test1 {

    @Autowired
    private UserMapper mapper;

    @Test
    public void test1(){
        /*QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("mobile","13316915805");
        User user = mapper.selectOne(wrapper);
        System.out.println(user);*/
        User user = mapper.selectById(1);
        System.out.println(user);
    }

    @Test
    public void json(){
        User user = new User();
        user.setId(1L);
        user.setMobile("15626542038");
        user.setPassword("123");
        System.out.println(JSON.toJSONString(user));
    }

}
