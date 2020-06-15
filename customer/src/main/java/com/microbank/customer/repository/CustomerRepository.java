package com.microbank.customer.repository;

import com.microbank.customer.model.Customer;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/** A MongoRepository for saving customer information. */
@Repository
public interface CustomerRepository extends MongoRepository<Customer, String> {}
