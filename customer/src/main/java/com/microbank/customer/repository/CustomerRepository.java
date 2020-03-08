package com.microbank.customer.repository;

import com.microbank.customer.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * A JpaRepository for saving user information.
 */
@Repository
public interface CustomerRepository extends JpaRepository<Customer, String> {}
