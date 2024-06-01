package com.enoca.enocaTask.service;

import com.enoca.enocaTask.entity.Cart;
import com.enoca.enocaTask.entity.Customer;
import com.enoca.enocaTask.repository.CustomerRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class CustomerService {
    private final CustomerRepository customerRepository;
    private final CartService cartService;
    public CustomerService(CustomerRepository customerRepository, CartService cartService) {
        this.customerRepository = customerRepository;
        this.cartService = cartService;
    }

    public List<Customer> getAllUsers() {
        return customerRepository.findAll();
    }

    public Customer getUserById(Long userId) {
        return customerRepository.findById(userId).orElse(null);
    }

    public Customer addCustomer(Customer customer) {
        return customerRepository.save(customer);
    }

    public Customer updateUser(Long userId,Customer newUser) {
    Customer updateCustomer = customerRepository.getReferenceById(userId);
        updateCustomer.setUsername(newUser.getUsername());
        updateCustomer.setSurname(newUser.getSurname());
        return customerRepository.save(updateCustomer);
    }

    @Transactional
    public void deleteUserById(Long userId) {
        Cart cart = cartService.getCartById(userId);
        cartService.deleteCart(userId);
        customerRepository.deleteById(userId);
    }
}
