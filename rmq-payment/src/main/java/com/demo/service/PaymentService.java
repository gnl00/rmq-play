package com.demo.service;

import com.demo.api.OrderApi;
import com.demo.entity.Order;
import com.demo.enumm.OrderStatus;
import com.demo.enumm.PayStatus;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * TODO
 *
 * @author gnl
 * @since 2023/4/18
 */
@Service
@Slf4j
public class PaymentService {

    @Autowired
    private RocketMQTemplate rocketMQTemplate;

    @Autowired
    private OrderApi orderApi;

    /**
     * 支付时不应该粗暴的返回 true 和 false
     * true 很好理解，支付成功
     * 但是 false 存在多种原因
     * 1、订单超时
     * 2、订单已被取消
     * 3、订单已支付
     * 应该定义对应的“支付状态”，才能更好的反应不同的支付情况
     */

    public int pay(Long orderId) {
        // check order status
        Order order = orderApi.getOrder(orderId);

        if (order == null) {
            return -99;
        }

        // order cancelled
        if (order.getStatus() == OrderStatus.CANCEL || order.getStatus() == OrderStatus.TIMEOUT_CANCEL) {
            log.info("Order {} CANCELED,  pay fail", orderId);
            return PayStatus.CANCELLED.getValue();
        }

        // order paid
        if (order.getStatus() == OrderStatus.PROCESSING || order.getStatus() == OrderStatus.DONE) {
            return PayStatus.PAID.getValue();
        }

        // try to pay order
        if (tryToPay(orderId)) {

            /**
             * 支付成功需要做什么：
             * 1、更新订单状态，更新操作如果没有执行成功，需要回滚事务
             * 2、更新物流信息
             */

            orderApi.updateOrderStatus(orderId, OrderStatus.PROCESSING.getValue());
            log.info("Order {} pay success", orderId);

            return PayStatus.SUCCESS.getValue();
        } else {
            // TODO rollback

            return PayStatus.FAILED.getValue();
        }
    }

    public boolean tryToPay(Long orderId) {
        return (int) (Math.random() * 10) < 5;
    }
}
