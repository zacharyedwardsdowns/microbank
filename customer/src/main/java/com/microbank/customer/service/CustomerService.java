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
 * A service to save and query customer data from the database. Is validated by the
 * ValidationService service.
 */
@Service
public class CustomerService {
  private CustomerRepository customerRepository;

  /**
   * Injects the necessary dependencies.
   *
   * @param customerRepository A MongoRepository for customers.
   */
  @Autowired
  public CustomerService(CustomerRepository customerRepository) {
    this.customerRepository = customerRepository;
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
