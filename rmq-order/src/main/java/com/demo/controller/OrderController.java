package com.demo.controller;

import com.demo.entity.Goods;
import com.demo.entity.Order;
import com.demo.enumm.OrderStatus;
import com.demo.mock.OrderDB;
import com.demo.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * OrderController
 *
 * @author gnl
 * @since 2023/4/18
 */
@RequestMapping("/order")
@RestController
@Slf4j
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/testJson")
    public void testJsonParams(@RequestBody Goods goods) {
        log.info(goods.toString());
    }

    @GetMapping("/pingOrd")
    boolean ping() {
        return true;
    }

    @GetMapping("/create/{goodsId}/{count}")
    public long createOrder(@PathVariable("goodsId") Long goodsId,@PathVariable("count") Long count) {
        return orderService.createOrder(goodsId, count);
    }

    @GetMapping("/all")
    public List<Order> getOrders() {
        return OrderDB.getOrders();
    }

    @GetMapping("/{orderId}")
    Order getOrder(@PathVariable("orderId") Long orderId) {
        return OrderDB.getOrder(orderId);
    }

    @GetMapping("/update/{orderId}/{orderStatus}")
    void updateOrderStatus(@PathVariable("orderId") Long orderId,@PathVariable("orderStatus") int orderStatus) {
        orderService.updateOrderStatus(orderId,orderStatus);
    }

}
