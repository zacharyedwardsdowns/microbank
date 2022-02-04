package com.microbank.customer.controller;

import com.microbank.customer.exception.ExistingCustomerException;
import com.microbank.customer.exception.FailedToRegisterCustomerException;
import com.microbank.customer.exception.InvalidJsonException;
import com.microbank.customer.exception.MissingRequirementsException;
import com.microbank.customer.exception.ResourceNotFoundException;
import com.microbank.customer.exception.ValidationException;
import com.microbank.customer.model.Customer;
import com.microbank.customer.security.Sanitizer;
import com.microbank.customer.security.model.Tokens;
import com.microbank.customer.service.CustomerService;
import com.microbank.customer.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/** Provides endpoints for verifying login, registering customers, and querying customer data. */
@RestController
public class CustomerController {
  private final CustomerService customerService;

  /**
   * Injects the necessary dependencies.
   *
   * @param customerService A service for the business logic of customer.
   */
  @Autowired
  public CustomerController(final CustomerService customerService) {
    this.customerService = customerService;
  }

  /**
   * Register a new customer in the database.
   *
   * @param customerJson The customer to register represented as a json string.
   * @return The information of the newly registered customer.
   * @throws ValidationException A validation error occurred.
   * @throws InvalidJsonException Failure to read the given json into Customer.
   * @throws ExistingCustomerException A customer already exists with the given username.
   * @throws FailedToRegisterCustomerException Failed to register the customer.
   */
  @PostMapping
  public ResponseEntity<Customer> register(@RequestBody String customerJson)
      throws ValidationException, ExistingCustomerException, InvalidJsonException,
          FailedToRegisterCustomerException {
    final Customer customer = Sanitizer.sanitizeAndMap(customerJson, Customer.class);
    return new ResponseEntity<>(customerService.register(customer), HttpStatus.CREATED);
  }

  /**
   * Get customer information from the database using their customerId.
   *
   * @param customerId The unique identifier of a customer.
   * @return The customer information for the given customerId.
   * @throws ResourceNotFoundException No customer exists for the given customerId.
   */
  @GetMapping("/{customerId}")
  public ResponseEntity<Customer> getCustomerByCustomerId(
      @PathVariable(name = "customerId") String customerId) throws ResourceNotFoundException {
    customerId = Sanitizer.sanitizeString(customerId);
    return new ResponseEntity<>(customerService.getCustomerByCustomerId(customerId), HttpStatus.OK);
  }

  /**
   * Deletes a customer from the database using their customerId.
   *
   * @param customerId The unique identifier of a customer to delete by.
   * @return The customer that was deleted.
   * @throws ResourceNotFoundException No customer exists for the given customerId.
   */
  @DeleteMapping("/{customerId}")
  public ResponseEntity<Customer> deleteCustomerByCustomerId(
      @PathVariable(name = "customerId") String customerId) throws ResourceNotFoundException {
    customerId = Sanitizer.sanitizeString(customerId);
    return new ResponseEntity<>(
        customerService.deleteCustomerByCustomerId(customerId), HttpStatus.OK);
  }

  /**
   * Authorizes the given user with an access token.
   *
   * @param customerJson Must contain a username and password.
   * @return An access token if given the correct credentials.
   * @throws InvalidJsonException Failure to read the given json into Customer.
   * @throws MissingRequirementsException Not given a username or password.
   */
  @PostMapping("${customer.request.authorize}")
  public ResponseEntity<Tokens> authorize(@RequestBody String customerJson)
      throws MissingRequirementsException, InvalidJsonException {
    final Customer customer = Sanitizer.sanitizeAndMap(customerJson, Customer.class);
    final String username = Sanitizer.sanitizeString(customer.getUsername());
    final String password = Sanitizer.sanitizeString(customer.getPassword());

    final Tokens tokens = customerService.verifyPasswordMatches(username, password);

    if (Util.tokensNotNull(tokens)) {
      return new ResponseEntity<>(tokens, HttpStatus.OK);
    } else {
      return new ResponseEntity<>(new Tokens(), HttpStatus.UNAUTHORIZED);
    }
  }
}
