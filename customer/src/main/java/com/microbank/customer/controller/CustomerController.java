package com.microbank.customer.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.microbank.customer.exception.*;
import com.microbank.customer.model.Customer;
import com.microbank.customer.security.Sanitizer;
import com.microbank.customer.service.CustomerService;
import com.microbank.customer.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/** Provides endpoints for verifying login, registering customers, and querying customer data. */
@RestController
public class CustomerController {
  private static final String INVALID_JSON =
      "Failed to create an instance of Customer with the given json!";
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
  @PostMapping("/customer")
  public ResponseEntity<Customer> register(@RequestBody String customerJson)
      throws ValidationException, ExistingCustomerException, InvalidJsonException,
          FailedToRegisterCustomerException {
    customerJson = Sanitizer.sanitizeJson(customerJson);
    final Customer customer;
    try {
      customer = Util.MAPPER.readValue(customerJson, Customer.class);
    } catch (final JsonProcessingException e) {
      throw new InvalidJsonException(INVALID_JSON, e);
    }
    return new ResponseEntity<>(customerService.register(customer), HttpStatus.CREATED);
  }

  /**
   * Get customer information from the database using their username.
   *
   * @param username The unique identifier of a customer.
   * @return The customer information for the given username.
   * @throws ResourceNotFoundException No customer exists for the given username.
   */
  @GetMapping("/customer/{username}")
  public ResponseEntity<Customer> getCustomerByUsername(
      @PathVariable(name = "username") String username) throws ResourceNotFoundException {
    username = Sanitizer.sanitizeString(username);
    return new ResponseEntity<>(customerService.getCustomerByUsername(username), HttpStatus.OK);
  }

  /**
   * Deletes a customer from the database using their username.
   *
   * @param username The unique identifier of a customer to delete by.
   * @return The customer that was deleted.
   * @throws ResourceNotFoundException No customer exists for the given username.
   */
  @DeleteMapping("/customer/{username}")
  public ResponseEntity<Customer> deleteCustomerByUsername(
      @PathVariable(name = "username") String username) throws ResourceNotFoundException {
    username = Sanitizer.sanitizeString(username);
    return new ResponseEntity<>(customerService.deleteCustomerByUsername(username), HttpStatus.OK);
  }

  /**
   * Verifies if the given password matches the password of the given user.
   *
   * @param username The username of the user to check for a password match against.
   * @param password The password to check matches.
   * @return Response code 204 if the password matches and 401 otherwise.
   * @throws ResourceNotFoundException No customer exists for the given username.
   * @throws MissingRequirementsException The password request header is null.
   */
  @GetMapping("/customer/{username}/password/match")
  public ResponseEntity<Void> verifyPasswordMatches(
      @PathVariable(name = "username") String username, @RequestHeader("password") String password)
      throws ResourceNotFoundException, MissingRequirementsException {
    username = Sanitizer.sanitizeString(username);
    password = Sanitizer.sanitizeString(password);
    if (customerService.verifyPasswordMatches(username, password)) {
      return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    } else {
      return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }
  }
}
