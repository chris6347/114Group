package com.itheima.dao;

import com.itheima.health.pojo.OrderSetting;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface OrderSettingDao {


    /**
     * 查询一段区间内的预约数据，一般是按月查询
     * @param begin
     * @param end
     * @return
     */
    List<OrderSetting> findOrderByMonth(@Param("begin") String begin , @Param("end")String end);

    /**
     * 根据日期来查询数据库里面的预约数据
     * @param date
     * @return
     */
    OrderSetting findByOrderDate(Date date);

    /**
     * 添加预约数据
     * @param date
     * @param number
     * @return
     */
    int add(@Param("date") Date date , @Param("number") int number);


    /**
     * 更新预约数据
     * @param date
     * @param number
     * @return
     */
    int update(@Param("date") Date date , @Param("number") int number);
}
