package com.enoca.enocaTask.controller;

import com.enoca.enocaTask.entity.*;
import com.enoca.enocaTask.repository.OrderRepository;
import com.enoca.enocaTask.service.CartService;
import com.enoca.enocaTask.service.CustomerService;
import com.enoca.enocaTask.service.OrderService;
import jakarta.transaction.Transactional;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }


    @GetMapping("/all")
    public ResponseEntity<List<Order>> getAllOrders() {
        return ResponseEntity.ok(orderService.getAllOrders());
    }

    @Transactional
    @PostMapping("/checkout/{cartId}")
    public ResponseEntity<String> placeOrder(@PathVariable Long cartId) {
        String message = orderService.placeOrder(cartId);
        return ResponseEntity.ok(message);
    }
}
