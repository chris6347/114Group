package com.itheima.test;

import com.itheima.QuickStartApplication;
import com.itheima.pojo.User;
import com.itheima.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

// 启动类包含了测试类所在包时,classes属性可以不写
@SpringBootTest(classes = QuickStartApplication.class)
@RunWith(SpringRunner.class)
public class Test01 {

    @Autowired
    private UserService userService;

    @Test
    public void test(){
        List<User> list = userService.findAll();
        System.out.println(list);
    }

}
