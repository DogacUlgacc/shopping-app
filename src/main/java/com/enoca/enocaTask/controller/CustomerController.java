package com.enoca.enocaTask.controller;

import com.enoca.enocaTask.dto.CustomerDto;
import com.enoca.enocaTask.entity.Customer;
import com.enoca.enocaTask.service.CartService;
import com.enoca.enocaTask.service.CustomerService;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/customer")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService userService, CartService cartService) {
        this.customerService = userService;
    }

    /*
     * Bütün Customerlar return ediliyor.
     * */
    @GetMapping("/all")
    public Page<Customer> getAllUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        return customerService.getAllUsers(page, size);
    }

    /*Id' ye göre customer return ediliyor. */
    @GetMapping("/{userId}")
    public Customer getUserById(@PathVariable Long userId) {
        return customerService.getUserById(userId);
    }


    @PostMapping("/add")
    public Customer addCustomer(@RequestBody Customer user) {
        return customerService.addCustomer(user);
    }

    /*
     * Customer update methodu isim ve soyisim değiştiriliyor.
     * */
    @PutMapping("/update/{userId}")
    public Customer updateUser(@PathVariable Long userId, @RequestBody CustomerDto newCustomer) {
        return customerService.updateUser(userId, newCustomer);
    }

    /*
     * Customer siliniyor.
     * Customer silinirken ona ait olan cart da siliniyor.
     * */
    @DeleteMapping("/delete/{userId}")
    public void deleteUserById(@PathVariable Long userId) {
        customerService.deleteUserById(userId);
    }
}
