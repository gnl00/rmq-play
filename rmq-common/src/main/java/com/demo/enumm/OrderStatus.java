package com.demo.enumm;

/**
 * TODO
 *
 * @author gnl
 * @since 2023/4/17
 */
public enum OrderStatus {

    INIT(0),

    PROCESSING(1),

    CANCEL(-1),

    TIMEOUT_CANCEL(-2),

    DONE(2);

    private int value;

    OrderStatus(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static OrderStatus lookup(int value) {
        for (OrderStatus orderStatus : OrderStatus.values()) {
            if (orderStatus.value == value) {
                return orderStatus;
            }
        }
        return null;
    }
}
