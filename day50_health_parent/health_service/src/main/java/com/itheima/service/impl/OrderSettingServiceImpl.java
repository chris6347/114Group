package com.itheima.service.impl;

import com.itheima.dao.OrderSettingDao;
import com.itheima.health.pojo.OrderSetting;
import com.itheima.service.OrderSettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class OrderSettingServiceImpl implements OrderSettingService {

    //注入进来dao
    @Autowired
    private OrderSettingDao dao;

    @Override
    public int updateOrderByDate(Date date, int number) {
        int row = 0 ;
        //1. 拿着日期去查询数据库，看一看数据库里面有没有这一天的预约数据。要查询出来这一天的数据
        OrderSetting osInDB = dao.findByOrderDate(date);

        //2. 判断这个对象是否存在，有还是没有？
        if(osInDB == null){ // 数据库里面没有这个日期的数据， 要做添加操作。！
            row = dao.add(date , number);
        }else{ //表明数据库里面有这个日期的数据， 要做更新操作。

            if(osInDB.getReservations() > number){
                System.out.println("可以预约的最大人数必须要大于已经预约的人数！");
                throw  new RuntimeException("可以预约的最大人数必须要大于已经预约的人数！");
                //return 0;
            }else{ //如果不大于，那么就可以更新数据了。
                row = dao.update(date , number);
            }
        }
        return row;
    }

    /**
     * 按月份来查询数据
     * @param year  2021
     * @param month 7
     * @return
     */
    @Override
    public List<OrderSetting> findOrderByMonth(String year, String month) {

        String begin = year+"-"+month+"-01"; //查询的月份开始值 2020-3-01
        String end = year+"-"+month+"-31"; // 查询的月份结束值 2020-3-31

        return dao.findOrderByMonth(begin, end);
    }

    /**
     *  批量导入预约信息到数据库
     *  1. 数据的解释：
     *      list集合包含了从excel表格里面读取到的所有内容。 里面装的是数组，一个数组即表示一个预约日期和可预约数量的整体
     *      数组的第0位，表示预约的日期
     *      数组的第1位，表示可预约的人数。
     *
     *  2. 数据库操作的分析：
     *      2.1 这里虽然说的是添加操作，但是不能盲目的认为就是以前的简单的添加。不能！
     *      2.2 因为从表格里面导入进来的预约数据，在数据库里面有可能存在！要判断的，可能是更新操作
     *
     *      2.3 批量导入数据库的操作其实就是添加或者更新的操作，到底是更新还是添加，就要查询数据库了。按照预约的日期来查询。
     *
     *          a. 拿着现在表里面读取到的日期，去数据库里面查询一下，按日期来查询，看看是否有这一天的预约数据。
     *          b. 如果有， 就做的是更新操作
     *              b1. 更新操作，也不能直接就更新，还要判断一下。
     *              b2. 表格里面的数据是最大可以预约的数量，如果数据库里面已经预约的数量 > 目前要设置的最大预约数量，那么禁止更新，
     *                  可以抛出一个异常，表示数据错误！
     *          c. 如果没有， 就做的是添加操作。
     * @param list
     * @return
     */
    @Override
    public int add(List<String[]> list) throws ParseException {

        int row = 0 ;
        //1. 判断不为空，并且预约的数据确实是有的
        if(list != null && list.size() > 0){

            SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");

            //2. 遍历集合，取出每一天的预约数据
            for (String[] strs : list) { //遍历出来的数据 0位是日期， 1位是预约数量。

                Date date = format.parse(strs[0]); // 日期 2021-07-12
                int number = Integer.parseInt(strs[1]); //最大可以预约数： 500


                //3. 拿着日期去查询数据库，看一看数据库里面有没有这一天的预约数据。要查询出来这一天的数据
                OrderSetting osInDB = dao.findByOrderDate(date);

                //4. 判断这个对象是否存在，有还是没有？
                if(osInDB == null){ // 数据库里面没有这个日期的数据， 要做添加操作。！
                    row += dao.add(date , number);
                }else{ //表明数据库里面有这个日期的数据， 要做更新操作。

                    if(osInDB.getReservations() > number){
                        System.out.println("可以预约的最大人数必须要大于已经预约的人数！");
                        throw  new RuntimeException("可以预约的最大人数必须要大于已经预约的人数！");
                        //return 0;
                    }else{ //如果不大于，那么就可以更新数据了。
                        row += dao.update(date , number);
                    }
                }
            }
        }
        return row == list.size() ? 1 : 0;
    }
}
