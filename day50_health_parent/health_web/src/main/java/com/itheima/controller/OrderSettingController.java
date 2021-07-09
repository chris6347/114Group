package com.itheima.controller;

import com.itheima.constant.MessageConstant;
import com.itheima.entity.Result;
import com.itheima.health.pojo.OrderSetting;
import com.itheima.service.OrderSettingService;
import com.itheima.utils.POIUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
public class OrderSettingController {

    @Autowired
    private OrderSettingService oss;

    @PutMapping("/ordersetting/{date}/{number}")
    public Result updateOrderByDate(@PathVariable("date") String date , @PathVariable("number") String number){

        try {
            SimpleDateFormat format  = new SimpleDateFormat("yyyy-MM-dd");

            int row = oss.updateOrderByDate(format.parse(date) , Integer.parseInt(number));

            Result result  = null;
            if(row >0 ){
                result = new Result(true , "更新预约数据成功");
            }else{
                result = new Result(false , "更新预约数据失败");

            }
            return result;
        } catch (ParseException e) {
            e.printStackTrace();
           return new Result(false , "更新预约数据失败");
        }
    }

    /**
     * 按月份来查询预约数据
     * @param year
     * @param month
     * @return
     */
    @GetMapping("/ordersetting/{year}/{month}")
    public Result findOrderByMonth(@PathVariable("year") String year , @PathVariable("month") String month){

        try {
            //1. 调用service
            List<OrderSetting> orderSettingList = oss.findOrderByMonth(year, month);


            //2. 由于前端页面要求的数据模型，比较特殊，日期只需要日子的数据，不要月份和年份，所以这里需要手动构造这些数据
            // [orderSetting对象1 , orderSetting对象2, orderSetting对象3...];
            List<Map<String , Object>> mapList = new ArrayList<>();
            for (OrderSetting orderSetting : orderSettingList) {

                Map<String , Object> map = new HashMap<>();
                map.put("date" , orderSetting.getOrderDate().getDate()); // 只需要日期里面的日子的数据，其他不要。
                map.put("number", orderSetting.getNumber());
                map.put("reservations" , orderSetting.getReservations());
                mapList.add(map);
            }

            //2. 是不是直接封装Result里面去呢？
            return new Result(true , "查询成功" , mapList);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false , "查询失败" );
        }
    }

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
