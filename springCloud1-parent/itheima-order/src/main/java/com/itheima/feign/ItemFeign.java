package com.itheima.feign;

import com.itheima.pojo.OrderInfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient(name = "item",path = "/item") // name= application.name 声明是item的客户端
//@RequestMapping("/item")
public interface ItemFeign {               // path : 提取了下面的getMapping中的第一段路径

    // Feign会根据注解帮我们生成URL地址
    @GetMapping("/{itemId}") // 与 itemController那个方法保持一致
    OrderInfo getOrderInfo(@PathVariable(name = "itemId") String itemId);
    // 返回值必须和 provider(item)的controller 一致
    // 请求路径必须和 provider的一致
    // 参数列表一致
    // 接口的方法的参数中必须带有注解,和注解对应传的参数名 (单个参数 高版本的feign组件已经不需要了)
}
