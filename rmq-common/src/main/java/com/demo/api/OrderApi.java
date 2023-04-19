package com.demo.api;

import com.demo.entity.Goods;
import com.demo.entity.Order;
import com.demo.enumm.OrderStatus;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * OrderApi
 *
 * @author gnl
 * @since 2023/4/18
 */
@FeignClient(name = "OrderApi", path = "/order", url = "http://localhost:8020")
@RestController
public interface OrderApi {

    @PostMapping("/testJson")
    void testJsonParams(@RequestBody Goods goods);

    @GetMapping("/pingOrd")
    boolean ping();

    @GetMapping("/create/{goodsId}/{count}")
    long createOrder(@PathVariable("goodsId") Long goodsId,@PathVariable("count") Long count);

    @GetMapping("/all")
    List<Order> getOrders();

    @GetMapping("/{orderId}")
    Order getOrder(@PathVariable("orderId") Long orderId);

    @GetMapping("/update/{orderId}/{orderStatus}")
    void updateOrderStatus(@PathVariable("orderId") Long orderId,@PathVariable("orderStatus") int orderStatus);
}
