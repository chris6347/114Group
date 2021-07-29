package com.itheima.controller;

import com.itheima.pojo.OrderInfo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/item")
public class ItemController {

    @GetMapping("/{itemId}")
    public OrderInfo getItemInfo(@PathVariable(name = "itemId") String itemId){
        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setItemId(itemId);
        orderInfo.setPrice(89L);
        orderInfo.setItemName("华为手机Mate30 - order 1");
        return orderInfo;
    }

}
