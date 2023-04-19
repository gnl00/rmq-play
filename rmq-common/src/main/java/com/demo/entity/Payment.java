package com.demo.entity;

import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

/**
 * Payment
 *
 * @author gnl
 * @since 2023/4/18
 */
@Data
@ToString
@RequiredArgsConstructor
public class Payment {
    @NonNull
    private Long payId;
    @NonNull
    private Long orderId;
}
