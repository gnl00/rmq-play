package com.demo.entity;

import com.demo.enumm.OrderStatus;
import lombok.*;
import org.springframework.lang.Nullable;

/**
 * Order
 *
 * @author gnl
 * @since 2023/4/17
 */
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Order {
    private Long orderId;
    private Goods goods;
    private OrderStatus status = OrderStatus.INIT;

    public Order(Long orderId, Goods goods) {
        this.orderId = orderId;
        this.goods = goods;
    }
}
