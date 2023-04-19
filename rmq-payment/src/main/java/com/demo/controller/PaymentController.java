package com.demo.controller;

import com.demo.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * PaymentController
 *
 * @author gnl
 * @since 2023/4/18
 */
@RestController
@RequestMapping("/payment")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @GetMapping("/pay/{orderId}")
    public int payOrder(@PathVariable(value = "orderId") Long orderId) {
        return paymentService.pay(orderId);
    }

    @GetMapping("/pingPayment")
    boolean ping() {
        return true;
    }

}
