package com.itheima.job;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class MyJob {

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

    @Scheduled(cron = "0/2 * * * * ?")
    public void jobA(){
        System.out.println("jobA"+sdf.format(new Date()));
    }

    @Scheduled(initialDelay = 3000,fixedRate = 5000)
    public void jobB(){
        System.out.println("jobB"+sdf.format(new Date()));
    }

}
