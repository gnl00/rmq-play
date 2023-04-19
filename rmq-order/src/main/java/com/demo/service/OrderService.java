package com.demo.service;

import com.demo.entity.Goods;
import com.demo.entity.Order;
import com.demo.enumm.OrderStatus;
import com.demo.mock.GoodsDB;
import com.demo.mock.OrderDB;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Random;


/**
 * OrderService
 *
 * @author gnl
 * @since 2023/4/17
 */
@Slf4j
@Service
public class OrderService {

    // 减库存，创建订单，进行本地事务，将结果保存到本地事务表
    // 然后发送 MQ 事务消息
    public long createOrder(Long goodsId, Long count) {
        long orderId = -1L;

        if ((int) (Math.random() * 10) < 5) {
            log.info("create order failed");
            return orderId;
        }

        if (GoodsDB.reduce(goodsId, count)) {
            orderId = (orderId = new Random().nextInt()) > 0 ? orderId : orderId * -1;
            Order order = new Order(orderId, new Goods(goodsId, count));
            OrderDB.insert(order);
            log.info("create order success, orderId: {}", orderId);
        }

        return orderId;
    }

    public void handleOrderListener(Long orderId) {
        checkPay(orderId);
    }

    // 查看超时订单是否已支付
    public void checkPay(Long orderId) {
        // get order from db
        Order order = OrderDB.getOrder(orderId);

        if (order == null) {
            log.info("Order {} not found", orderId);
            return;
        }

        // unpaid
        if (order.getStatus() == OrderStatus.INIT) {
            // set to timeout_cancel
            order.setStatus(OrderStatus.TIMEOUT_CANCEL);
        }

        // paid or canceled do nothing
        // ...

        log.info("[checkPay] => OrderStatus: {}", order.getStatus().getValue());
    }

    public void updateOrderStatus(Long orderId, int orderStatus) {
        OrderStatus ordS = OrderStatus.lookup(orderStatus);
        OrderDB.getOrder(orderId).setStatus(ordS);
    }
}
