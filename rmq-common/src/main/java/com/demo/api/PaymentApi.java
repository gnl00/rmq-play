package com.demo.api;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * TODO
 *
 * @author gnl
 * @since 2023/4/18
 */
@FeignClient(name = "PaymentApi", path = "/payment", url = "http://localhost:8030")
@RestController
public interface PaymentApi {
    @GetMapping("/pay/{orderId}")
    int payOrder(@PathVariable(value = "orderId") Long orderId);

    @GetMapping("/pingPayment")
    boolean ping();
}
