package com.itheima.job;


import com.itheima.dao.SetMealDao;
import com.itheima.utils.QiNiuUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/*
    这是一个任务，用来请清除七牛服务器上面的垃圾图片
 */
@Component
public class CleanImgJob {

    @Autowired
    private SetMealDao dao ;

    /**
     * 目标：
     *      清除七牛的垃圾图片
     *  分析：
     *      如何定义垃圾图片
     *      1. 把数据库里面所有图片给查询出来  a.jpg b.jpg c.jpg
     *      2. 把七牛云上面的所有图片给查询出来 aa.jpg bb.jpg c.jpg
     *      3. 让七牛云的图片  -  数据库的图片  =  垃圾图片。
     */
    public void clean7NiuImgJob(){

        //1. 查询数据库得到所有的图片
        List<String> dbList = dao.findAllImg(); // {a.jpg b.jpg c.jpg}
        System.out.println("数据库的图片：" + dbList);

        //2. 查询七牛上面的所有图片
        List<String> qiniuList = QiNiuUtils.listFile(); //{aa.jpg bb.jpg c.jpg}
        System.out.println("七牛的图片：" + qiniuList);

        //3. 裁剪出来垃圾图片
        boolean flag = qiniuList.removeAll(dbList);
        QiNiuUtils.removeFiles(qiniuList.toArray(new String[]{}));

    }
}
