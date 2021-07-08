package com.itheima.controller;

import com.itheima.constant.MessageConstant;
import com.itheima.entity.Result;
import com.itheima.health.pojo.CheckGroup;
import com.itheima.health.pojo.Setmeal;
import com.itheima.service.SetMealService;
import com.itheima.utils.QiNiuUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
public class SetMealController {

    @Autowired
    private SetMealService ss;

    @PostMapping("/setmeal/{checkgroupIds}")
    public Result add(@RequestBody Setmeal setmeal , @PathVariable("checkgroupIds") int [] checkgroupIds){

        System.out.println("setmeal=" + setmeal);
        System.out.println("checkgroupIds=" + Arrays.toString(checkgroupIds));

        //1. 调用service
        int row = ss.add(setmeal, checkgroupIds);

        //2. 响应
        Result result = null;
        if(row >0 ){
            result = new Result( true ,MessageConstant.ADD_SETMEAL_SUCCESS);
        }else{
            result = new Result( false ,MessageConstant.ADD_SETMEAL_FAIL);
        }
        return result;
    }


    /**
     * 上传图片到七牛云
     * @param imgFile
     * @return
     */
    @PostMapping("/setmeal/image")
    public Result upload(MultipartFile imgFile){
        System.out.println("来上传图片了~~");

        try {

            //1. 定义新的文件名字
            String oldName = imgFile.getOriginalFilename();  //heima.jpg
            String newName = UUID.randomUUID().toString() + oldName.substring(oldName.lastIndexOf('.'));//aiuwelrjljs-k2jk3ljadf.jpg


            //2. 直接上传图片
            QiNiuUtils.uploadViaByte(imgFile.getBytes() , newName);

            /*
                3. 给页面响应
                    3.1 上传图片成功之后，要给前端页面把这个图片的地址给返回回去。
                    3.2 只有 这样，前端的页面才能把图片给预览展示出来
                    3.3 在这里就要把图片的地址给写出去了。但是光写出去图片的地址还不够。
                    3.4 上传图片不能光想着预览图片，还要考虑一会做新增套餐的时候，把数据保存到msyql数据库的时候
                        图片以什么样的形式保存到数据库去。
                   3.5 由于文件已经以实体的方式保存在了七牛云里面，所以MySQL数据库只要保存图片的名字|地址即可。
             */

            //在这里最好用一个map集合封装数据，拆分域名和文件名。
            Map<String , String> map = new HashMap<String ,String >();
            map.put("domain", QiNiuUtils.DOMAIN);
            map.put("fileName", newName); // data:{flag:true, message:'上传成功', data:{domain:"七牛的域名", fileName:"文件名"}}

            return new Result(true , MessageConstant.PIC_UPLOAD_SUCCESS , map);
        } catch (IOException e) {
            e.printStackTrace();
            return new Result(false , MessageConstant.PIC_UPLOAD_FAIL );
        }
    }
}
