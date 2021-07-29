package com.itheima.comtroller;

import com.alibaba.cloud.nacos.discovery.NacosServiceDiscovery;
import com.alibaba.nacos.api.exception.NacosException;
import com.itheima.feign.ItemFeign;
import com.itheima.pojo.OrderInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import java.util.List;

@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private NacosServiceDiscovery nacosServiceDiscovery;

    @Autowired
    private ItemFeign itemFeign;

    @GetMapping("/{username}")
    public OrderInfo getOrderInfo(@PathVariable String username) throws NacosException {
        String orderId = "OrderId:123456";
        System.out.println("模拟获取到订单的ID==="+orderId);
        String itemId = "SN:789";
        System.out.println("模拟获取到商品的ID==="+itemId);
        //OrderInfo orderInfo = restTemplate.getForObject("http://localhost:18081/item/"+itemId,OrderInfo.class);
        /*List<ServiceInstance> item = nacosServiceDiscovery.getInstances("item");
        ServiceInstance serviceInstance = item.get(0);
        OrderInfo orderInfo = restTemplate.getForObject("http://" + serviceInstance.getHost() + ":" + serviceInstance.getPort() + "/item/" + itemId, OrderInfo.class);*/
        /*OrderInfo orderInfo = restTemplate.getForObject("http://item/item/" + itemId, OrderInfo.class);*/ //第一个item是spring.application.name= 即服务名
        OrderInfo orderInfo = itemFeign.getOrderInfo(itemId);
        orderInfo.setUsername(username);
        orderInfo.setOrderId(orderId);
        return orderInfo;
    }

}
