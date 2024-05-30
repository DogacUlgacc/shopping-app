package com.enoca.enocaTask.controller;

import com.enoca.enocaTask.entity.Cart;
import com.enoca.enocaTask.entity.Customer;
import com.enoca.enocaTask.service.CartService;
import com.enoca.enocaTask.service.CustomerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/customer")
public class CustomerController {

    private final CustomerService customerService;
    private final CartService cartService;

    public CustomerController(CustomerService userService, CartService cartService) {
        this.customerService = userService;
        this.cartService = cartService;
    }

    @GetMapping("/all")
    public List<Customer> getAllUsers(){
        return customerService.getAllUsers();
    }

    @GetMapping("/{userId}")
    public Customer getUserById(@PathVariable Long userId){
        return customerService.getUserById(userId);
    }

    @PostMapping("/add")
    public ResponseEntity<Object> AddCustomer(@RequestBody Customer user) {
        try {
            Customer addedUser = customerService.addCustomer(user);
            Cart cart = new Cart();
            cart.setCustomer(addedUser); // Müşteriyi sepete ata
            cart.setTotalPrice(0.0); // Başlangıçta toplam fiyatı sıfır yapabiliriz

            // Alışveriş sepetini veritabanına ekle

            Cart addedCart = cartService.addCart(cart);

            return ResponseEntity.status(HttpStatus.CREATED).body(addedUser);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Customer eklenirken bir hata oluştu.");
        }
    }

    @PutMapping("/{userId}")
    public Customer updateUser(@PathVariable Long userId, @RequestBody Customer newCustomer) {
        return customerService.updateUser(userId,newCustomer);
    }

    @DeleteMapping("/{userId}")
    public void deleteUserById(@PathVariable Long userId){
        customerService.deleteUserById(userId);
    }
}
