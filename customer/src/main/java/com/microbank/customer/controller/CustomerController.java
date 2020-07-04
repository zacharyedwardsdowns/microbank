package com.microbank.customer.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.microbank.customer.exception.ExistingCustomerException;
import com.microbank.customer.exception.InvalidJsonException;
import com.microbank.customer.exception.ValidationException;
import com.microbank.customer.model.Customer;
import com.microbank.customer.security.Sanitizer;
import com.microbank.customer.service.CustomerService;
import com.microbank.customer.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/** Provides endpoints for verifying login, registering customers, and querying customer data. */
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
   * @param customerJson The customer to register represented as a json string.
   * @return The information of the newly registered customer.
   * @throws ValidationException Thrown if a validation error occurs.
   * @throws InvalidJsonException Thrown upon failure to read the given json into Customer.
   * @throws ExistingCustomerException Thrown if a customer already exists with the given username.
   */
  @PostMapping("register")
  public ResponseEntity<Customer> register(@RequestBody String customerJson)
      throws ValidationException, ExistingCustomerException, InvalidJsonException {
    customerJson = Sanitizer.sanitizeJson(customerJson);
    Customer customer;
    try {
      customer = Util.MAPPER.readValue(customerJson, Customer.class);
    } catch (JsonProcessingException e) {
      throw new InvalidJsonException(
          "Failed to create an instance of Customer with the given json!", e);
    }
    return new ResponseEntity<>(customerService.register(customer), HttpStatus.CREATED);
  }
}
