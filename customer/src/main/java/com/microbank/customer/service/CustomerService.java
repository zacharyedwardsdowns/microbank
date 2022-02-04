package com.microbank.customer.service;

import com.microbank.customer.exception.ExistingCustomerException;
import com.microbank.customer.exception.FailedToRegisterCustomerException;
import com.microbank.customer.exception.MissingRequirementsException;
import com.microbank.customer.exception.ResourceNotFoundException;
import com.microbank.customer.exception.ValidationException;
import com.microbank.customer.model.Customer;
import com.microbank.customer.repository.CustomerRepository;
import com.microbank.customer.security.PasswordEncoder;
import com.microbank.customer.security.TokenGenerator;
import com.microbank.customer.security.model.Tokens;
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
  private final TokenGenerator tokenGenerator;
  private static final int ATTEMPTS = 3;

  /**
   * Injects the necessary dependencies.
   *
   * @param customerRepository A MongoRepository for customers.
   * @param validationService Validates customer information before contacting the database.
   * @param tokenGenerator Generates tokens for authorization endpoint.
   */
  @Autowired
  public CustomerService(
      final CustomerRepository customerRepository,
      final ValidationService validationService,
      final TokenGenerator tokenGenerator) {
    this.customerRepository = customerRepository;
    this.validationService = validationService;
    this.tokenGenerator = tokenGenerator;
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

    if (customerRepository.exists(query)) {
      throw new ExistingCustomerException(
          "A customer already exists with the username " + customer.getUsername() + "!");
    } else {
      customer.setJoinedOn(Util.currentTime());
      customer.setLastUpdatedOn(customer.getJoinedOn());
      customer.setPassword(PasswordEncoder.generateHash(customer.getPassword()));
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

    final Optional<Customer> customer = customerRepository.findOne(query);

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
    customerRepository.delete(deleteCustomer);
    LOG.debug("Successfully deleted customer by customerId!");
    return deleteCustomer;
  }

  /**
   * Verifies if the given password matches the password of the given user.
   *
   * @param username The username of the user to check for a password match against.
   * @param password The password to check matches.
   * @return True if the password matches and false otherwise.
   * @throws MissingRequirementsException The username or password parameter is null.
   */
  public Tokens verifyPasswordMatches(final String username, final String password)
      throws MissingRequirementsException {
    if (Util.nullOrEmpty(password)) {
      throw new MissingRequirementsException("Must provide a password to authorize!");
    }
    if (Util.nullOrEmpty(username)) {
      throw new MissingRequirementsException("Must provide a username to authorize!");
    }
    try {
      final Customer customer = getCustomerByUsername(username);
      if (PasswordEncoder.matchesHash(password, customer.getPassword())) {
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

    final Optional<Customer> customer = customerRepository.findOne(query);

    if (customer.isPresent()) {
      return customer.get();
    } else {
      throw new ResourceNotFoundException("No customer exists with the username " + username + "!");
    }
  }

  /**
   * Generates a new JWT token for an authorized user.
   *
   * @param customer the customer to generate tokens for.
   * @return A JWT token containing relevant information.
   */
  private Tokens generateJwtToken(final Customer customer) {
    return tokenGenerator.generateTokens(customer);
  }
}
