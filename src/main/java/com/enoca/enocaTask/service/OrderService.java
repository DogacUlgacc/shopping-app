package com.enoca.enocaTask.service;

import com.enoca.enocaTask.entity.*;
import com.enoca.enocaTask.repository.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final CartService cartService;
    private final CustomerService customerService;

    public OrderService(OrderRepository orderRepository, CartService cartService, CustomerService customerService) {
        this.orderRepository = orderRepository;
        this.cartService = cartService;
        this.customerService = customerService;
    }


    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public String placeOrder(Long cartId) {
        Cart cart = cartService.getCartById(cartId);
        Order order = new Order();
        Customer customer = customerService.getUserById(cartId);
        List<OrderItem> orderItems = new ArrayList<>();

        for (CartItem cartItem : cart.getItems()) {
            OrderItem orderItem = new OrderItem();

            orderItem.setProduct(cartItem.getProduct());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setOrder(order);
            orderItem.setPrice(cartItem.getProduct().getPrice());

            orderItems.add(orderItem);
        }
        order.setOrderItems(orderItems);
        order.setCustomer(customer);
        order.setTotalPrice(cart.getTotalPrice());

        orderRepository.save(order);
        return "Sipariş başarıyla oluşturuldu";
    }


    public List<Order> getAllOrdersForCustomer(Long customerId) {
        return orderRepository.findByCustomerId(customerId);

    }
}