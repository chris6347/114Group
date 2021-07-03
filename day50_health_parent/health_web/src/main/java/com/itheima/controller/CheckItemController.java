package com.itheima.controller;

/*
    这是用来处理检查项的controller

        1. 添加操作 ,
            a. 传递上来的是json数据，要想把json数据接收转化到一个javaBean身上，需要加上注解

 */

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.Page;
import com.itheima.constant.MessageConstant;
import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.entity.Result;
import com.itheima.health.pojo.CheckItem;
import com.itheima.service.CheckItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

//@Controller // 现在就不打@controller注解了，因为我们所有的后台方法返回的都是json字符串，不会有页面的跳转。
@RestController // @RestController = @Controller + @ResponseBody
@RequestMapping("/checkItem")
public class CheckItemController {

    //让spring注入进来service
    @Autowired
    private CheckItemService cs;


    /**
     * localhost:83/checkItem/findPage.do
     * 分页查询
     * @param queryPageBean 封装了我们分页查询的条件：包含3个：  currentPage , pageSize , queryString
     * @return
     */
    @RequestMapping("/findPage")
    public Result findPage(@RequestBody QueryPageBean queryPageBean){
        Result result   = null;

        try {
            //1. 调用service
            PageResult<CheckItem> pageResult = cs.findPage(queryPageBean);

            //2. 给页面响应
            result = new Result(true , MessageConstant.QUERY_CHECKITEM_SUCCESS ,pageResult);
        } catch (Exception e) {
            e.printStackTrace();
            result = new Result(false , MessageConstant.QUERY_CHECKITEM_FAIL);
        }

        return result;
    }

    /**
     * 新增检查项
     * @param checkItem
     * @return
     */
    @RequestMapping("/add")
    public Result add(@RequestBody CheckItem checkItem){

        //1. 调用service 干活
        int row = cs.add(checkItem);

        //2. 给页面响应
        Result result = null;
        if(row > 0 ){
            result = new Result(true , MessageConstant.ADD_CHECKITEM_SUCCESS);
        }else{
           result = new Result(false , MessageConstant.ADD_CHECKITEM_FAIL);
        }
        return result;
    }
}
