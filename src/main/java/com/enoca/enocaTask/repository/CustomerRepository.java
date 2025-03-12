package com.enoca.enocaTask.repository;

import com.enoca.enocaTask.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepository  extends JpaRepository<Customer,Long> {

    Optional<Customer> findByEmail(String email);
}
