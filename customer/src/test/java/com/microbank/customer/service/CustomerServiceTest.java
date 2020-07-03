package com.microbank.customer.service;

import com.microbank.customer.repository.CustomerRepository;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;

public class CustomerServiceTest {

  private static CustomerRepository mockCustomerRepository;
  private CustomerService customerService;

  @BeforeClass
  public static void setupClass() throws Exception {
    mockCustomerRepository = Mockito.mock(CustomerRepository.class);
  }

  @Before
  public void testConstructor() throws Exception {
    customerService = new CustomerService(mockCustomerRepository);
  }

  @Test
  public void testRegister() throws Exception {}
}
