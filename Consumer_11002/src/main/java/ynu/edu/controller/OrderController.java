package ynu.edu.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ynu.edu.entity.Order;

import java.util.Arrays;
import java.util.Date;

@RestController
@RequestMapping("/order")
public class OrderController {

    @GetMapping("/getOrderById/{orderId}")
    public Order getOrderById(@PathVariable("orderId") Long orderId) {
        Order order = new Order();
        order.setOrderId(orderId);
        order.setUserId(1001);
        order.setProductList(Arrays.asList("苹果手机", "华为手表"));
        order.setTotalAmount(8999.00);
        order.setStatus("PAID");
        order.setCreateTime(new Date());

        return order;
    }
}