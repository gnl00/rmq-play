package com.demo.mock;

import com.demo.entity.Order;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * TODO
 *
 * @author gnl
 * @since 2023/4/19
 */

@Component
@Slf4j
public class OrderDB {

    private static final Map<Long, Order> db = new HashMap<>();

    public static List<Order> getOrders() {
        return new ArrayList<>(db.values());
    }

    public static Order getOrder(Long orderId) {
        return db.get(orderId);
    }

    public static int insert(Order order) {
        Order ret = db.put(order.getOrderId(), order);
        return ret != null ? 1 : -1;
    }

    public static int update(Order order) {
        return insert(order);
    }

}
