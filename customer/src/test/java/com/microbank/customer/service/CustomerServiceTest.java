package com.microbank.customer.service;

import com.microbank.customer.exception.ExistingCustomerException;
import com.microbank.customer.exception.MissingRequirementsException;
import com.microbank.customer.exception.ResourceNotFoundException;
import com.microbank.customer.model.Customer;
import com.microbank.customer.repository.CustomerRepository;
import com.microbank.customer.security.Sanitizer;
import com.microbank.customer.util.Util;
import java.io.File;
import java.nio.file.Files;
import java.util.Optional;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.core.io.ClassPathResource;

public class CustomerServiceTest {

  private static CustomerRepository mockCustomerRepository;
  private static ValidationService mockValidationService;
  private CustomerService customerService;
  private static String json;
  private Customer customer;

  @BeforeClass
  public static void setupClass() throws Exception {
    mockCustomerRepository = Mockito.mock(CustomerRepository.class);
    mockValidationService = Mockito.mock(ValidationService.class);

    final File resource = new ClassPathResource("json/Customer.json").getFile();
    json = Files.readString(resource.toPath());
    json = Sanitizer.sanitizeJson(json);
  }

  @Before
  public void setup() throws Exception {
    customerService = new CustomerService(mockCustomerRepository, mockValidationService);
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

  @Test
  public void testGetCustomerByUsername() throws Exception {
    Mockito.when(mockCustomerRepository.findOne(Mockito.any())).thenReturn(Optional.of(customer));
    final Customer result = customerService.getCustomerByUsername(customer.getUsername());
    Assert.assertEquals(customer, result);
  }

  @Test(expected = ResourceNotFoundException.class)
  public void testGetCustomerByUsernameResourceNotFoundException() throws Exception {
    Mockito.when(mockCustomerRepository.findOne(Mockito.any())).thenReturn(Optional.empty());
    customerService.getCustomerByUsername(customer.getUsername());
  }

  @Test
  public void testDeleteCustomerByUsername() throws Exception {
    Mockito.when(mockCustomerRepository.findOne(Mockito.any())).thenReturn(Optional.of(customer));
    final Customer result = customerService.deleteCustomerByUsername(customer.getUsername());
    Mockito.verify(mockCustomerRepository, Mockito.times(1)).delete(customer);
    Assert.assertEquals(customer, result);
  }

  @Test(expected = ResourceNotFoundException.class)
  public void testDeleteCustomerByUsernameResourceNotFoundException() throws Exception {
    Mockito.when(mockCustomerRepository.findOne(Mockito.any())).thenReturn(Optional.empty());
    customerService.deleteCustomerByUsername(customer.getUsername());
  }

  @Test
  public void testVerifyCustomerExists() throws Exception {
    Mockito.when(mockCustomerRepository.findOne(Mockito.any())).thenReturn(Optional.of(customer));
    customerService.verifyCustomerExists(customer);
    Mockito.verify(mockCustomerRepository, Mockito.times(1)).findOne(Mockito.any());
  }

  @Test(expected = ResourceNotFoundException.class)
  public void testVerifyCustomerExistsResourceNotFoundException() throws Exception {
    Mockito.when(mockCustomerRepository.findOne(Mockito.any())).thenReturn(Optional.empty());
    customerService.verifyCustomerExists(customer);
  }

  @Test(expected = MissingRequirementsException.class)
  public void testVerifyCustomerExistsMissingRequirementsException() throws Exception {
    customerService.verifyCustomerExists(new Customer());
  }
}
