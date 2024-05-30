package com.enoca.enocaTask.controller;

import com.enoca.enocaTask.entity.Order;
import com.enoca.enocaTask.service.OrderService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/order")
public class OrderController {
    /*private final OrderService orderService;

    public OrderController(OrderService orderService){
        this.orderService = orderService;
    }

    @GetMapping("/all")
    public List<Order> getAllOrders(){
        return orderService.getAllOrders();
    }

    @GetMapping("{userId}")
    public List<Order> getOrdersByUserId(@PathVariable Long userId){
        return orderService.getOrderByUserId(userId);
    }



   *//* @PostMapping("/add")
    public Order addOrder(@RequestBody OrderDto orderDto){
        return orderService.addOrder(orderDto);
    }*//*

    @PutMapping("/{orderId}")
    public Order updateOrder(@RequestBody Order newOrder, @PathVariable Long orderId){
        newOrder.setId(orderId);
        return orderService.updateOrder(newOrder);
    }
    @DeleteMapping("/{orderId}")
    public void deleteOrderById(@PathVariable Long orderId){
        orderService.deleteOrderById(orderId);
    }*/
}
