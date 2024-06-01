package com.enoca.enocaTask.service;

import com.enoca.enocaTask.entity.OrderItem;
import com.enoca.enocaTask.repository.OrderItemRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderItemService {

    private final OrderItemRepository orderItemRepository;

    public OrderItemService(OrderItemRepository orderItemRepository) {
        this.orderItemRepository = orderItemRepository;
    }

    public List<OrderItem> getAllOrderItems() {
        return orderItemRepository.findAll();
    }

    public OrderItem getOrderItemById(Long id) {
        return orderItemRepository.findById(id).orElseThrow(() -> new RuntimeException("OrderItem bulunamadı"));
    }

    public List<OrderItem> findByOrderId(Long orderId) {
        return orderItemRepository.findByOrderId(orderId);
    }
}
