package com.demo.enumm;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/**
 * TODO
 *
 * @author gnl
 * @since 2023/4/19
 */
@Getter
@ToString
@AllArgsConstructor
public enum PayStatus {
    FAILED(-2),
    CANCELLED(-1),
    UNPAID(0),
    PAID(1),
    SUCCESS(2);

    private int value;
}
