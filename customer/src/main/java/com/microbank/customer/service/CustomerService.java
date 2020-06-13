package com.microbank.customer.service;

import com.microbank.customer.exception.ExistingCustomerException;
import com.microbank.customer.model.Customer;
import com.microbank.customer.repository.CustomerRepository;
import com.microbank.customer.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

/**
 * A service to save and query customer data from the database. Is validated by
 * the ValidationService service.
 */
@Service
public class CustomerService {
  private CustomerRepository customerRepository;

  /**
   * Injects the necessary dependencies.
   *
   * @param customerRepository A MongoRepository for users.
   */
  @Autowired
  public CustomerService(CustomerRepository customerRepository) {
    this.customerRepository = customerRepository;
  }

  /**
   * Register a new user in the database.
   *
   * @param user The user to register.
   * @return The information of the newly registered user.
   */
  public Customer register(Customer customer) throws ExistingCustomerException {
    Customer searchCustomer = new Customer();
    searchCustomer.setUsername(customer.getUsername());
    Example<Customer> query = Example.of(customer, Util.defaultMatcher());

    if (this.customerRepository.exists(query)) {
      throw new ExistingCustomerException();
    } else {
      return this.customerRepository.insert(customer);
    }
  }
}
