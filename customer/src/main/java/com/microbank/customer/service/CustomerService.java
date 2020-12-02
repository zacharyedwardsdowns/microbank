package com.microbank.customer.service;

import com.microbank.customer.exception.ExistingCustomerException;
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
   * @throws ValidationException Thrown if a validation error occurs.
   * @throws ExistingCustomerException Thrown if a customer already exists with the given username.
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
      final Customer result = this.customerRepository.insert(customer);
      removePassword(result);
      return result;
    }
  }

  /**
   * Retrieves a customer from the database by their username.
   *
   * @param username The username to search with.
   * @return The customer with the given username.
   * @throws ResourceNotFoundException Thrown if no customer exists with the given username.
   */
  public Customer getCustomerByUsername(final String username) throws ResourceNotFoundException {
    final Customer searchCustomer = new Customer();
    searchCustomer.setUsername(username);
    final Example<Customer> query = Example.of(searchCustomer, Util.defaultMatcher());

    final Optional<Customer> customer = this.customerRepository.findOne(query);

    if (customer.isPresent()) {
      final Customer result = customer.get();
      removePassword(result);
      return result;
    } else {
      throw new ResourceNotFoundException("No customer exists with the username " + username + "!");
    }
  }

  /**
   * Exists to explain why the password is being set to null. Removes the password from the
   * response. (Should look into making it an Aspect)
   *
   * @param customer The customer to remove the password from.
   */
  private void removePassword(final Customer customer) {
    customer.setPassword(null);
  }
}
