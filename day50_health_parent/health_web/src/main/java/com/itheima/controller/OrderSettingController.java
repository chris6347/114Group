package com.itheima.controller;

import com.itheima.entity.Result;
import com.itheima.service.OrderSettingService;
import com.itheima.utils.POIUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@RestController
public class OrderSettingController {

    @Autowired
    private OrderSettingService oss;

    /**
     * 批量导入Excel表格的内容
     * @param excelFile
     * @return
     */
    @PostMapping("/ordersetting/file")
    public Result uploadFile(MultipartFile excelFile){

        try {
            /*
                1. 读取文件 ，读取到了所有预约信息内容
                2. 表里面可能有多个预约的日期数据，所以返回的是一个List集合
                3. 预约的内容包含了预约的日期，以及可以预约的人数 ，这里使用一个数组来封装
                    数组的第0位，表示预约的日期
                    数组的第1位，表示可预约的人数。
                     strs=[2021/07/12, 500]
                     strs=[2021/07/13, 400]
             */
            List<String[]> list = POIUtils.readExcel(excelFile);

            //2. 遍历
            /*for (String[] strs : list) {
                System.out.println("strs=" + Arrays.toString(strs));
            }*/
            int row = oss.add(list);
            if(row >0 ){
                return new Result(true, "上传成功");
            }else{
                return new Result(false, "上传失败");
            }

        }catch (RuntimeException e){
            return new Result(false, e.getMessage());
        }catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "上传失败");
        }
    }
}
