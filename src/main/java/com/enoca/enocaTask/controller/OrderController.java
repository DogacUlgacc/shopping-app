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

    /*
    * Bütün Orderlar return edliyor.
    * */
    @GetMapping("/all")
    public ResponseEntity<List<Order>> getAllOrders() {
        return ResponseEntity.ok(orderService.getAllOrders());
    }


    /*
    * Customer_id alınarak o Customer'ın sahip olduğu Orderlar return ediliyor.
    * */
    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<Order>> getAllOrdersForCustomer(@PathVariable Long customerId) {
        List<Order> orders = orderService.getAllOrdersForCustomer(customerId);
        return ResponseEntity.ok(orders);
    }

    /*
     * Belirli bir sepet (cartId) için sipariş oluşturur.
     * Sepetteki ürünler Order olarak dönüştürülür ve toplam fiyat güncellenir.
     * Sipariş oluşturduktan sonra CartItemlar silinir ve cart içerisindeki total price da sıfırlanır.
     */
    @Transactional
    @PostMapping("/checkout/{cartId}")
    public ResponseEntity<String> PlaceOrder(@PathVariable Long cartId) {
        String message = orderService.placeOrder(cartId);
        return ResponseEntity.ok(message);
    }

    /*
     * OrderId kullanarak veritabanından sipariş bilgilerini return eder.
     */
    @GetMapping("{orderId}")
    public ResponseEntity<Order> GetOrderForCode(@PathVariable Long orderId){
         Order order = orderService.GetOrderForCode(orderId);
         if(order != null){
             return ResponseEntity.ok(order);
         }
         else {
             return  ResponseEntity.notFound().build();
         }
     }
}

