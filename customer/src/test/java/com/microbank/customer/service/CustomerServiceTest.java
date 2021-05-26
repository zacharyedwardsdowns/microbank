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
  public void testGetCustomerByCustomerId() throws Exception {
    Mockito.when(mockCustomerRepository.findOne(Mockito.any())).thenReturn(Optional.of(customer));
    final Customer result = customerService.getCustomerByCustomerId(customer.getCustomerId());
    Assert.assertEquals(customer, result);
  }

  @Test(expected = ResourceNotFoundException.class)
  public void testGetCustomerByCustomerIdResourceNotFoundException() throws Exception {
    Mockito.when(mockCustomerRepository.findOne(Mockito.any())).thenReturn(Optional.empty());
    customerService.getCustomerByCustomerId(customer.getCustomerId());
  }

  @Test
  public void testDeleteCustomerByCustomerId() throws Exception {
    Mockito.when(mockCustomerRepository.findOne(Mockito.any())).thenReturn(Optional.of(customer));
    final Customer result = customerService.deleteCustomerByCustomerId(customer.getCustomerId());
    Mockito.verify(mockCustomerRepository, Mockito.times(1)).delete(customer);
    Assert.assertEquals(customer, result);
  }

  @Test(expected = ResourceNotFoundException.class)
  public void testDeleteCustomerByCustomerIdResourceNotFoundException() throws Exception {
    Mockito.when(mockCustomerRepository.findOne(Mockito.any())).thenReturn(Optional.empty());
    customerService.deleteCustomerByCustomerId(customer.getCustomerId());
  }

  @Test
  public void verifyPasswordMatches() throws Exception {
    Mockito.when(mockCustomerRepository.findOne(Mockito.any())).thenReturn(Optional.of(customer));
    final boolean response =
        customerService.verifyPasswordMatches(customer.getUsername(), customer.getPassword());
    Assert.assertTrue(response);
  }

  @Test
  public void verifyPasswordMatchesFalse() throws Exception {
    Mockito.when(mockCustomerRepository.findOne(Mockito.any())).thenReturn(Optional.of(customer));
    final boolean response =
        customerService.verifyPasswordMatches(customer.getUsername(), customer.getPassword() + "1");
    Assert.assertFalse(response);
  }

  @Test
  public void verifyPasswordMatchesOfNonExistentUser() throws Exception {
    Mockito.when(mockCustomerRepository.findOne(Mockito.any())).thenReturn(Optional.empty());
    customerService.verifyPasswordMatches(customer.getUsername(), customer.getPassword());
  }

  @Test(expected = MissingRequirementsException.class)
  public void verifyPasswordMatchesMissingRequirementsException() throws Exception {
    customerService.verifyPasswordMatches(customer.getUsername(), null);
  }
}
