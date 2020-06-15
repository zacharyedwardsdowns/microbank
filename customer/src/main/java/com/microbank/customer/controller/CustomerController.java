package com.microbank.customer.controller;

import com.microbank.customer.exception.ExistingCustomerException;
import com.microbank.customer.model.Customer;
import com.microbank.customer.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/** Provides endpoints for verifying login, registering users, and querying customer data. */
@RestController
public class CustomerController {
  private CustomerService customerService;

  /**
   * Injects the necessary dependencies.
   *
   * @param customerService A service for the business logic of customer.
   */
  @Autowired
  public CustomerController(CustomerService customerService) {
    this.customerService = customerService;
  }

  /**
   * Register a new customer in the database.
   *
   * @param user The customer to register.
   * @return The information of the newly registered customer.
   */
  @PostMapping("register")
  public Customer register(@RequestBody Customer customer) throws ExistingCustomerException {
    return customerService.register(customer);
  }
}
