package com.enoca.enocaTask.controller;

import com.enoca.enocaTask.entity.*;

import com.enoca.enocaTask.service.OrderService;
import jakarta.transaction.Transactional;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<Order>> getAllOrdersForCustomer(@PathVariable Long customerId) {
        List<Order> orders = orderService.getAllOrdersForCustomer(customerId);
        return ResponseEntity.ok(orders);
    }

    @Transactional
    @PostMapping("/checkout/{cartId}")
    public ResponseEntity<String> PlaceOrder(@PathVariable Long cartId) {
        String message = orderService.placeOrder(cartId);
        return ResponseEntity.ok(message);
    }


    @GetMapping("/{orderItemId}")
    public ResponseEntity<List<OrderItem>> GetOrderForCode(@PathVariable Long orderItemId) {
        List<OrderItem> orderItems = orderService.getOrderItemsByOrderId(orderItemId);
        if (orderItems != null && !orderItems.isEmpty()) {
            return ResponseEntity.ok(orderItems);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
