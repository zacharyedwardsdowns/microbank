package com.microbank.customer.service;

import com.microbank.customer.exception.ExistingCustomerException;
import com.microbank.customer.exception.MissingRequirementsException;
import com.microbank.customer.exception.ResourceNotFoundException;
import com.microbank.customer.exception.ValidationException;
import com.microbank.customer.model.Customer;
import com.microbank.customer.repository.CustomerRepository;
import com.microbank.customer.util.Util;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

/**
 * A service to save and query customer data from a database. Is validated by
 * ValidationService.java.
 */
@Service
public class CustomerService {
  private final CustomerRepository customerRepository;
  private final ValidationService validationService;

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
   */
  public Customer register(final Customer customer)
      throws ValidationException, ExistingCustomerException {
    validationService.validateNewCustomer(customer);

    final Customer searchCustomer = new Customer();
    searchCustomer.setUsername(customer.getUsername());
    final Example<Customer> query = Example.of(searchCustomer, Util.defaultMatcher());

    if (this.customerRepository.exists(query)) {
      throw new ExistingCustomerException(
          "A customer already exists with the username " + customer.getUsername() + "!");
    } else {
      return this.customerRepository.insert(customer);
    }
  }

  /**
   * Retrieves a customer from the database by their username.
   *
   * @param username The username to search with.
   * @return The customer with the given username.
   * @throws ResourceNotFoundException No customer exists with the given username.
   */
  public Customer getCustomerByUsername(final String username) throws ResourceNotFoundException {
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
   * Deletes a customer using the given username as a unique identifier.
   *
   * @param username The username of the customer to delete.
   * @return The deleted customer.
   * @throws ResourceNotFoundException No customer exists with the given username.
   */
  public Customer deleteCustomerByUsername(final String username) throws ResourceNotFoundException {
    final Customer deleteCustomer = getCustomerByUsername(username);
    this.customerRepository.delete(deleteCustomer);
    return deleteCustomer;
  }

  /**
   * Verifies a customer exists using the getCustomerByUsername() method.
   *
   * @param customer A Customer object that should contain the username of the customer to verify.
   * @throws ResourceNotFoundException No customer exists for the given username.
   * @throws MissingRequirementsException The username field of the given Customer is null.
   */
  public void verifyCustomerExists(final Customer customer)
      throws ResourceNotFoundException, MissingRequirementsException {
    if (customer.getUsername() == null) {
      throw new MissingRequirementsException(
          "Must provide a username to verify if a customer exists!");
    }
    getCustomerByUsername(customer.getUsername());
  }
}
