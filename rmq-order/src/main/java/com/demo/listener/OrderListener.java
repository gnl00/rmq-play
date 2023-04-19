package com.demo.listener;

import com.demo.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * OrderListener
 *
 * @author gnl
 * @since 2023/4/18
 */
@Slf4j
@Component
@RocketMQMessageListener(consumerGroup = "${spring.application.name}", topic = "${rmq-play.topic}", selectorExpression = "${rmq-play.tag}")
public class OrderListener implements RocketMQListener<Long> {

    @Value("${rmq-play.topic}")
    private String topic;

    @Autowired
    private OrderService orderService;

    // 收到消息说明订单已经超时
    @Override
    public void onMessage(Long orderId) {
        // check order step
        log.info("[{} receive delay] orderId {}", topic, orderId);
        if (orderId > 0) orderService.handleOrderListener(orderId);
    }
}
