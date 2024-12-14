package com.enoca.enocaTask.service;

import com.enoca.enocaTask.dto.CustomerDto;
import com.enoca.enocaTask.entity.Cart;
import com.enoca.enocaTask.entity.Customer;
import com.enoca.enocaTask.repository.CustomerRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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
        Customer addedUser = customerRepository.save(customer);
        Cart cart = new Cart();
        cart.setCustomer(addedUser); // Müşteriye sepeti ata
        cart.setTotalPrice(0.0); // Başlangıçta toplam fiyatı sıfır

        // Alışveriş sepetini veritabanına ekle
        cartService.addCart(cart);
        return customerRepository.getReferenceById(customer.getId());
    }

    public Customer updateUser(Long userId, CustomerDto newUser) {
        Optional<Customer> optionalCustomer = customerRepository.findById(userId);
        if (optionalCustomer.isEmpty()) {
            throw new EntityNotFoundException("Customer with ID " + userId + " not found");
        }
        Customer updateCustomer = optionalCustomer.get();
        updateCustomer.setEmail(newUser.getEmail());
        return customerRepository.save(updateCustomer);
    }

    @Transactional
    public void deleteUserById(Long userId) {
        cartService.deleteCart(userId);
        customerRepository.deleteById(userId);
    }
}
