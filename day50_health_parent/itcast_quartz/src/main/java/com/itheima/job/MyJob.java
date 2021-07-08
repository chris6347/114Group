package com.itheima.job;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 这是一个工作任务 这个任务就是打日志。
 */
public class MyJob {
    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public void print(){
        System.out.println("打印日志：...."+format.format(new Date()));
    }
}
