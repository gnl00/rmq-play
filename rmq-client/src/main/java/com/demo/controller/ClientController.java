package com.demo.controller;

import com.demo.api.OrderApi;
import com.demo.api.PaymentApi;
import com.demo.entity.Goods;
import com.demo.entity.Order;
import com.demo.enumm.PayStatus;
import com.demo.mock.GoodsDB;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ClientController
 *
 * @author gnl
 * @since 2023/4/18
 */
@Slf4j
@RequestMapping("/client")
@RestController
public class ClientController {
    public Map<String, Object> results = new HashMap<>();

    {
        results.put("code", 200);
        results.put("success", true);
    }

    @GetMapping("/testJson")
    public void testJson() {
        orderApi.testJsonParams(new Goods(100L, 200L));
    }

    @GetMapping("/test")
    public String test() {
        return "rmq-client [test]";
    }

    @Value("${rmq-play.topic-test}")
    private String clientToPayTestTopic;

    @GetMapping("/testRMQ")
    public void testRMQ() {
        rocketMQTemplate.convertAndSend(clientToPayTestTopic, "hello this is a test message");
    }

    @GetMapping("/pingOrd")
    public Map<String, Object> pingOrder() {
        if (orderApi.ping()) {
            results.put("message", "[pong] from order service");
        } else {
            results.put("message", "[ping] order service failed");
        }
        return results;
    }

    @GetMapping("/pingPmt")
    public Map<String, Object> pingPayment() {
        if (paymentApi.ping()) {
            results.put("message", "[pong] from payment service");
        } else {
            results.put("message", "[ping] payment service failed");
        }
        return results;
    }

    @Resource
    private OrderApi orderApi;


    @Resource
    private RocketMQTemplate rocketMQTemplate;

    @Value("${rmq-play.topic}")
    private String clientOrderTopic;

    @GetMapping("/order/{goodsId}/{count}")
    public Map<String, Object> createOrder(@PathVariable(value = "goodsId") Long goodsId, @PathVariable(value = "count") Long count) {

        // check goods store
        if (!checkGoodsStore(goodsId, count)) {
            log.info("GoodsId {} not enough", goodsId);
            results.put("message", "create order failed");
            return results;
        }

        // create a new order
        long orderId = doCreateOrder(goodsId, count);
        if (orderId != -1) {
            results.put("message", "create order success");
            results.put("data", "orderId: " + orderId);
        } else {
            results.put("message", "create order failed");
            results.put("data", null);
        }
        return results;
    }

    public boolean checkGoodsStore(Long goodsId, Long count) {
        long store = GoodsDB.getStore(goodsId);
        return store >= count;
    }

    @GetMapping("/orders")
    List<Order> getOrders() {
        return orderApi.getOrders();
    }

    public long doCreateOrder(Long goodsId, Long count) {
        long orderId = orderApi.createOrder(goodsId, count);
        if (orderId > 0) {
            // delay send to mq
            log.info("delay send to mq");
            sendToOrder(orderId);
            return orderId;
        }
        return orderId;
    }

    public void sendToOrder(Long orderId) {
        // send json content
        // Order order = DB.getOrder(orderId);
        // ObjectMapper objectMapper = new ObjectMapper();
        // String jsonMessage = objectMapper.writeValueAsString(order);

        // 如果使用 org.apache.rocketmq.common.message.Message
        // send after 3 minutes
        // int deliveryTimeStamp = (int) (System.currentTimeMillis() + 3L * 60 * 1000);
        // Map<String, Object> msgConfig = new HashMap<>();
        // msgConfig.put("DELAY", deliveryTimeStamp);

        // 如果使用 springframework.messaging.Message
        Message<Long> message = MessageBuilder.withPayload(orderId).build();
        // convertAndSend 底层使用 syncSend
        rocketMQTemplate.asyncSend(clientOrderTopic, message, new SendCallback() {
            @Override
            public void onSuccess(SendResult sendResult) {
                log.info("[rocketmq] send order onSuccess");
            }

            @Override
            public void onException(Throwable e) {
                // what if send to mq failed?
                // 如果 mq 发送失败，订单是否也创建失败呢？
                // 首先明确一点：在这个业务逻辑中，mq 发送的是延迟消息，用来判断订单是否支付。
                // 如果订单已支付，do nothing；如果订单未支付，则将订单状态修改成 timeout_cancelled 表示订单超时未付款，被取消了。
                // 现在得到关键信息：修改订单状态。
                // 是否只有这一种办法修改呢？不是的，除了 mq 延迟消息，还可以使用别的办法来修改订单状态，比如前端直接发送请求。
                // 所以 mq 发送失败和订单创建失败这两个业务逻辑不应该绑定在一起

                log.info("[rocketmq] send order onException {}", e.getCause().toString());
            }
        // }, 3000);
        }, 50000, 5); // delay level 1=1s 2=5s 3=10s 4=30s 5=1m 6=2m ... 3m 4m 5m 6m 7m 8m 9m 10m 20m 30m 1h 2h
    }

    @GetMapping("/pay/{orderId}")
    public Map<String, Object> payOrder(@PathVariable(value = "orderId") Long orderId) {
        results.put("method", "[payOrder]");

        if (orderId < 0 || doPay(orderId) == PayStatus.CANCELLED.getValue()) {
            results.put("message", "pay fail");
        }

        if (doPay(orderId) == PayStatus.PAID.getValue()) {
            results.put("message", "paid");
        }

        if (doPay(orderId) == PayStatus.SUCCESS.getValue()) {
            results.put("message", "pay success");
        }

        return results;
    }

    @Resource
    private PaymentApi paymentApi;

    public int doPay(Long orderId) {
        return paymentApi.payOrder(orderId);
    }

}
