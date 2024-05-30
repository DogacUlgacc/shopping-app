package com.enoca.enocaTask.repository;

import com.enoca.enocaTask.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository  extends JpaRepository<Customer,Long> {
}
