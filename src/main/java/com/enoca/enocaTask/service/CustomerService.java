package com.enoca.enocaTask.service;

import com.enoca.enocaTask.dto.CustomerDto;
import com.enoca.enocaTask.entity.Cart;
import com.enoca.enocaTask.entity.Customer;
import com.enoca.enocaTask.repository.CustomerRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

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

    public Page<Customer> getAllUsers(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("name").ascending());
        return customerRepository.findAll(pageable);
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
        Customer updateCustomer = customerRepository.findById(userId)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND, "Customer with ID " + userId + " not found"));
        // Müşteri bulunduysa bilgilerini güncelle
        updateCustomer.setEmail(newUser.getEmail());
        // Güncellenmiş müşteriyi veritabanına kaydet
        return customerRepository.save(updateCustomer);
    }

    @Transactional
    public void deleteUserById(Long userId) {
        cartService.deleteCart(userId);
        customerRepository.deleteById(userId);
    }
}
