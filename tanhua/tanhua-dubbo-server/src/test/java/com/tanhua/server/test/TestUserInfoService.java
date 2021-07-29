package com.tanhua.server.test;

import com.tanhua.domain.db.User;
import com.tanhua.domain.db.UserInfo;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TestUserInfoService {

    /**
     * 查询全部用户
     */
    @Cacheable(value="users")
    public List<UserInfo> findAll() {
        System.out.println("从数据库获取数据");
        //模拟从数据库查询
        List<UserInfo> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            UserInfo info = new UserInfo();
            info.setId((long)i);
            info.setNickname("itcast"+i);
            list.add(info);
        }
        return list;
    }

    /**
     * key ： user:2
     *  @Cacheable:
     *      value : 缓存的名称
     *      key：支持spel，可以获取方法上的参数
     */
    @Cacheable(value="user",key = "#userId")  // #userId : 参数中的userId
    public UserInfo findById(Long userId) {
        System.out.println("从数据库获取数据");
        UserInfo info = new UserInfo();
        info.setNickname("abc");
        info.setId(userId);
        return info;
    }

    /**
     * value指定了缓存存放在哪块命名空间
     * key使用的是spEL表达式
     * 此处的UserInfo实体类一定要实现序列化
     * */

    /**
     * 保存用户
     */
    @CacheEvict(value = "users",allEntries=true)
    public void save(UserInfo userInfo) {
        System.out.println("保存用户到数据库中");
    }

    // #user.id: 参数中的user 的Id
    @CacheEvict(value="user",key = "#user.id")
    public void update(UserInfo user) {
        System.out.println("更新");
    }

}

