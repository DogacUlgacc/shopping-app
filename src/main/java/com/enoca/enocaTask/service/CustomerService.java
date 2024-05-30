package com.enoca.enocaTask.service;

import com.enoca.enocaTask.entity.Customer;
import com.enoca.enocaTask.repository.CustomerRepository;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class CustomerService {
    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
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

    public void deleteUserById(Long userId) {
        customerRepository.deleteById(userId);
    }
}
