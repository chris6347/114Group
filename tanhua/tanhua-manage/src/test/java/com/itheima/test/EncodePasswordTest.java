package com.itheima.test;

import cn.hutool.core.date.DateUtil;
import cn.hutool.crypto.SecureUtil;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Date;

public class EncodePasswordTest {

    @Test
    public void test(){
        System.out.println(SecureUtil.md5("123456"));
    }

    @Test
    public void test2(){
        System.out.println(System.currentTimeMillis());
        System.out.println(DateUtil.offsetDay(new Date(),-30));
        System.out.println(DateUtil.yesterday().toDateStr());
    }

    @Test
    public void test3(){
        BigDecimal bigDecimal = BigDecimal.valueOf(1.1);
        BigDecimal divide = bigDecimal.divide(BigDecimal.valueOf(0.12), 3, BigDecimal.ROUND_HALF_UP);
        System.out.println(divide);

    }
    /*
    * docker run -d -p 9876:9876 -v /Users/chris/work/docker/data/namesrv/logs:/root/logs -v /Users/chris/work/docker/data/namesrv/store:/root/store --name rmqnamesrv -e "MAX_POSSIBLE_HEAP=100000000" rocketmqinc/rocketmq:4.4.0 sh mqnamesrv
    *docker run -d -e "JAVA_OPTS=-Drocketmq.namesrv.addr=rmqnamesrv:9876 -Dcom.rocketmq.sendMessageWithVIPChannel=false" --link rmqnamesrv:rmqnamesrv -p 8080:8080 -t apacherocketmq/rocketmq-console:2.0.0
    * /Library/Java/JavaVirtualMachines/zulu-8.jdk/Contents/Home
    * */

    public void test4(){

    }

}
