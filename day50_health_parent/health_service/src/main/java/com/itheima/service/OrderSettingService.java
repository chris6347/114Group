package com.itheima.service;

import com.itheima.health.pojo.OrderSetting;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

public interface OrderSettingService {

    /**
     * 按日期来更新预约数据
     * @param date
     * @param number
     * @return
     */
    int updateOrderByDate(Date date , int number);

    /**
     * 按月份来查询预约数据
     * @param year
     * @param month
     * @return
     */
    List<OrderSetting> findOrderByMonth(String year , String month);

    /**
     * 批量导入预约信息
     * @param list
     * @return
     */
    int add( List<String[]> list) throws ParseException;
}
