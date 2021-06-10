package com.microbank.customer.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.microbank.customer.exception.ExistingCustomerException;
import com.microbank.customer.exception.FailedToRegisterCustomerException;
import com.microbank.customer.exception.MissingRequirementsException;
import com.microbank.customer.exception.ResourceNotFoundException;
import com.microbank.customer.exception.ValidationException;
import com.microbank.customer.model.Customer;
import com.microbank.customer.repository.CustomerRepository;
import com.microbank.customer.security.model.Token;
import com.microbank.customer.util.Util;
import java.util.Optional;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

/**
 * A service to save and query customer data from a database. Is validated by
 * ValidationService.java.
 */
@Service
public class CustomerService {
  private static final Logger LOG = LoggerFactory.getLogger(CustomerService.class);
  private final CustomerRepository customerRepository;
  private final ValidationService validationService;
  private static final int ATTEMPTS = 3;

  /**
   * Injects the necessary dependencies.
   *
   * @param customerRepository A MongoRepository for customers.
   * @param validationService Validates customer information before contacting the database.
   */
  @Autowired
  public CustomerService(
      final CustomerRepository customerRepository, final ValidationService validationService) {
    this.customerRepository = customerRepository;
    this.validationService = validationService;
  }

  /**
   * Register a new customer in the database.
   *
   * @param customer The customer to register.
   * @return The information of the newly registered customer.
   * @throws ValidationException A validation error occurred.
   * @throws ExistingCustomerException A customer already exists with the given username.
   * @throws FailedToRegisterCustomerException Failed to register the customer after 3 attempts.
   */
  public Customer register(final Customer customer)
      throws ValidationException, ExistingCustomerException, FailedToRegisterCustomerException {
    validationService.validateNewCustomer(customer);

    final Customer searchCustomer = new Customer();
    searchCustomer.setUsername(customer.getUsername());
    final Example<Customer> query = Example.of(searchCustomer, Util.defaultMatcher());

    if (this.customerRepository.exists(query)) {
      throw new ExistingCustomerException(
          "A customer already exists with the username " + customer.getUsername() + "!");
    } else {
      customer.setJoinedOn(Util.currentTime());
      customer.setLastUpdatedOn(customer.getJoinedOn());
      Customer registeredCustomer = null;

      for (int i = 0; i < ATTEMPTS; i++) {
        customer.setCustomerId(UUID.randomUUID().toString());
        try {
          registeredCustomer = customerRepository.insert(customer);
          break;
        } catch (final DuplicateKeyException e) {
          LOG.warn("Failed to generate a unique UUID!");
        }
      }

      if (registeredCustomer == null) {
        throw new FailedToRegisterCustomerException(
            "Failed to register a customer after " + ATTEMPTS + " attempts!");
      } else {
        LOG.debug("Successfully registered customer!");
      }
      return registeredCustomer;
    }
  }

  /**
   * Retrieves a customer from the database by their customerId.
   *
   * @param customerId The customerId to search with.
   * @return The customer with the given customerId.
   * @throws ResourceNotFoundException No customer exists with the given customerId.
   */
  public Customer getCustomerByCustomerId(final String customerId)
      throws ResourceNotFoundException {
    final Customer searchCustomer = new Customer();
    searchCustomer.setCustomerId(customerId);
    final Example<Customer> query = Example.of(searchCustomer, Util.defaultMatcher());

    final Optional<Customer> customer = this.customerRepository.findOne(query);

    if (customer.isPresent()) {
      LOG.debug("Successfully retrieved customer by customerId!");
      return customer.get();
    } else {
      throw new ResourceNotFoundException(
          "No customer exists with the customerId " + customerId + "!");
    }
  }

  /**
   * Deletes a customer using the given customerId as a unique identifier.
   *
   * @param customerId The username of the customer to delete.
   * @return The deleted customer.
   * @throws ResourceNotFoundException No customer exists with the given customerId.
   */
  public Customer deleteCustomerByCustomerId(final String customerId)
      throws ResourceNotFoundException {
    final Customer deleteCustomer = getCustomerByCustomerId(customerId);
    this.customerRepository.delete(deleteCustomer);
    LOG.debug("Successfully deleted customer by customerId!");
    return deleteCustomer;
  }

  /**
   * Verifies if the given password matches the password of the given user.
   *
   * @param username The username of the user to check for a password match against.
   * @param password The password to check matches.
   * @return True if the password matches and false otherwise.
   * @throws MissingRequirementsException The password parameter is null.
   */
  public Token verifyPasswordMatches(final String username, final String password)
      throws MissingRequirementsException {
    if (Util.nullOrEmpty(password)) {
      throw new MissingRequirementsException("Must provide a password to check if it matches!");
    }
    try {
      final Customer customer = getCustomerByUsername(username);
      if (customer.getPassword().equals(password)) {
        return generateJwtToken(customer);
      } else {
        return null;
      }
    } catch (final ResourceNotFoundException e) {
      return null;
    }
  }

  /**
   * Retrieves a customer from the database by their username. For use within this service only.
   *
   * @param username The username to search with.
   * @return The customer with the given username.
   * @throws ResourceNotFoundException No customer exists with the given username.
   */
  private Customer getCustomerByUsername(final String username) throws ResourceNotFoundException {
    final Customer searchCustomer = new Customer();
    searchCustomer.setUsername(username);
    final Example<Customer> query = Example.of(searchCustomer, Util.defaultMatcher());

    final Optional<Customer> customer = this.customerRepository.findOne(query);

    if (customer.isPresent()) {
      return customer.get();
    } else {
      throw new ResourceNotFoundException("No customer exists with the username " + username + "!");
    }
  }

  /**
   * Generates a new JWT token for an authenticated user.
   *
   * @param customer the customer to generate a token for.
   * @return A JWT token containing relevant information.
   */
  private Token generateJwtToken(final Customer customer) {
    return new Token(
        JWT.create()
            .withClaim("customerId", customer.getCustomerId())
            .sign(Algorithm.HMAC256("token")));
  }
}
