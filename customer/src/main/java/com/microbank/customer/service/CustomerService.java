package com.microbank.customer.service;

import com.microbank.customer.exception.ExistingCustomerException;
import com.microbank.customer.exception.ValidationException;
import com.microbank.customer.model.Customer;
import com.microbank.customer.repository.CustomerRepository;
import com.microbank.customer.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

/**
 * A service to save and query customer data from a database. Is validated by
 * ValidationService.java.
 */
@Service
public class CustomerService {
  private CustomerRepository customerRepository;
  private ValidationService validationService;

  /**
   * Injects the necessary dependencies.
   *
   * @param customerRepository A MongoRepository for customers.
   * @param validationService Validates customer information before contacting the database.
   */
  @Autowired
  public CustomerService(
      CustomerRepository customerRepository, ValidationService validationService) {
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
  public Customer register(Customer customer)
      throws ValidationException, ExistingCustomerException {
    validationService.validateNewCustomer(customer);

    Customer searchCustomer = new Customer();
    searchCustomer.setUsername(customer.getUsername());
    Example<Customer> query = Example.of(customer, Util.defaultMatcher());

    if (this.customerRepository.exists(query)) {
      throw new ExistingCustomerException(
          "A customer already exists with the username " + customer.getUsername() + "!");
    } else {
      return this.customerRepository.insert(customer);
    }
  }
}
