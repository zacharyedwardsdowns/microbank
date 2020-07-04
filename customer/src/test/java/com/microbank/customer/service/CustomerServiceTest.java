package com.microbank.customer.service;

import com.microbank.customer.exception.ExistingCustomerException;
import com.microbank.customer.model.Customer;
import com.microbank.customer.repository.CustomerRepository;
import com.microbank.customer.security.Sanitizer;
import com.microbank.customer.util.Util;
import java.io.File;
import java.nio.file.Files;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.core.io.ClassPathResource;

public class CustomerServiceTest {

  private static CustomerRepository mockCustomerRepository;
  private CustomerService customerService;
  private static String json;
  private Customer customer;

  @BeforeClass
  public static void setupClass() throws Exception {
    mockCustomerRepository = Mockito.mock(CustomerRepository.class);

    final File resource = new ClassPathResource("json/Customer.json").getFile();
    json = Files.readString(resource.toPath());
    json = Sanitizer.sanitizeJson(json);
  }

  @Before
  public void setup() throws Exception {
    customerService = new CustomerService(mockCustomerRepository);
    customer = Util.MAPPER.readValue(json, Customer.class);
  }

  @Test
  public void testRegister() throws Exception {
    Mockito.when(mockCustomerRepository.insert(customer)).thenReturn(customer);
    final Customer result = customerService.register(customer);
    Assert.assertEquals(customer, result);
  }

  @Test(expected = ExistingCustomerException.class)
  public void testRegisterExistingCustomerException() throws Exception {
    Mockito.when(mockCustomerRepository.exists(Mockito.any())).thenReturn(true);
    customerService.register(customer);
  }
}
