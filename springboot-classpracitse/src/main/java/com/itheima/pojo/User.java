package com.itheima.pojo;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.io.Serializable;
                                                 // prefix: 只读前缀下的配置,属性名与配置名一一对应
@ConfigurationProperties(prefix = "person.a")    // 读取配置放到bean对象里 , 依赖set方法
@Component      // 容器先注册bean                  // 封装框架时用
public class User implements Serializable {

    private String name;
    private String age;

                                                     public String getName() {
                                                         return name;
                                                     }

                                                     public void setName(String name) {
                                                         this.name = name;
                                                     }

                                                     public String getAge() {
                                                         return age;
                                                     }

                                                     public void setAge(String age) {
                                                         this.age = age;
                                                     }

                                                     @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", age='" + age + '\'' +
                '}';
    }
}
