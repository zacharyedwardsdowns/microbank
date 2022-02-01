package com.microbank.customer.service;

import com.microbank.customer.exception.ExistingCustomerException;
import com.microbank.customer.exception.FailedToRegisterCustomerException;
import com.microbank.customer.exception.MissingRequirementsException;
import com.microbank.customer.exception.ResourceNotFoundException;
import com.microbank.customer.model.Customer;
import com.microbank.customer.repository.CustomerRepository;
import com.microbank.customer.security.PasswordEncoder;
import com.microbank.customer.security.Sanitizer;
import com.microbank.customer.security.model.Token;
import com.microbank.customer.util.Util;
import java.io.File;
import java.nio.file.Files;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.core.io.ClassPathResource;
import org.springframework.dao.DuplicateKeyException;

class CustomerServiceTest {

  private static CustomerRepository mockCustomerRepository;
  private static ValidationService mockValidationService;
  private CustomerService customerService;
  private static String json;
  private Customer customer;

  @BeforeAll
  static void setupClass() throws Exception {
    mockCustomerRepository = Mockito.mock(CustomerRepository.class);
    mockValidationService = Mockito.mock(ValidationService.class);

    final File resource = new ClassPathResource("json/Customer.json").getFile();
    json = Files.readString(resource.toPath());
    json = Sanitizer.sanitizeJson(json);
  }

  @BeforeEach
  void setup() throws Exception {
    customerService =
        new CustomerService(mockCustomerRepository, mockValidationService, "signature");
    customer = Util.MAPPER.readValue(json, Customer.class);
  }

  @Test
  void testRegister() throws Exception {
    Mockito.when(mockCustomerRepository.insert(customer)).thenReturn(customer);
    final Customer result = customerService.register(customer);
    Assertions.assertEquals(customer, result);
  }

  @Test()
  void testRegisterDuplicateKeyException() {
    Mockito.when(mockCustomerRepository.exists(Mockito.any())).thenReturn(false);
    Mockito.when(mockCustomerRepository.insert(ArgumentMatchers.any(Customer.class)))
        .thenThrow(DuplicateKeyException.class);
    Assertions.assertThrows(
        FailedToRegisterCustomerException.class, () -> customerService.register(customer));
  }

  @Test()
  void testRegisterExistingCustomerException() {
    Mockito.when(mockCustomerRepository.exists(Mockito.any())).thenReturn(true);
    Assertions.assertThrows(
        ExistingCustomerException.class, () -> customerService.register(customer));
  }

  @Test
  void testGetCustomerByCustomerId() throws Exception {
    Mockito.when(mockCustomerRepository.findOne(Mockito.any())).thenReturn(Optional.of(customer));
    final Customer result = customerService.getCustomerByCustomerId(customer.getCustomerId());
    Assertions.assertEquals(customer, result);
  }

  @Test()
  void testGetCustomerByCustomerIdResourceNotFoundException() {
    Mockito.when(mockCustomerRepository.findOne(Mockito.any())).thenReturn(Optional.empty());
    Assertions.assertThrows(
        ResourceNotFoundException.class,
        () -> customerService.getCustomerByCustomerId(customer.getCustomerId()));
  }

  @Test
  void testDeleteCustomerByCustomerId() throws Exception {
    Mockito.when(mockCustomerRepository.findOne(Mockito.any())).thenReturn(Optional.of(customer));
    final Customer result = customerService.deleteCustomerByCustomerId(customer.getCustomerId());
    Mockito.verify(mockCustomerRepository, Mockito.times(1)).delete(customer);
    Assertions.assertEquals(customer, result);
  }

  @Test()
  void testDeleteCustomerByCustomerIdResourceNotFoundException() {
    Mockito.when(mockCustomerRepository.findOne(Mockito.any())).thenReturn(Optional.empty());
    Assertions.assertThrows(
        ResourceNotFoundException.class,
        () -> customerService.deleteCustomerByCustomerId(customer.getCustomerId()));
  }

  @Test
  void verifyPasswordMatches() throws Exception {
    final String password = customer.getPassword();
    customer.setPassword(PasswordEncoder.generateHash(customer.getPassword()));
    Mockito.when(mockCustomerRepository.findOne(Mockito.any())).thenReturn(Optional.of(customer));
    final Token response = customerService.verifyPasswordMatches(customer.getUsername(), password);
    Assertions.assertNotNull(response);
  }

  @Test
  void verifyPasswordMatchesFalse() throws Exception {
    final String password = customer.getPassword();
    customer.setPassword(PasswordEncoder.generateHash(customer.getPassword()));
    Mockito.when(mockCustomerRepository.findOne(Mockito.any())).thenReturn(Optional.of(customer));
    final Token response =
        customerService.verifyPasswordMatches(customer.getUsername(), password + "1");
    Assertions.assertNull(response);
  }

  @Test
  void verifyPasswordMatchesOfNonExistentUser() throws Exception {
    Mockito.when(mockCustomerRepository.findOne(Mockito.any())).thenReturn(Optional.empty());
    customerService.verifyPasswordMatches(customer.getUsername(), customer.getPassword());
  }

  @Test()
  void verifyPasswordMatchesUsernameMissingRequirementsException() {
    Assertions.assertThrows(
        MissingRequirementsException.class,
        () -> customerService.verifyPasswordMatches(null, customer.getPassword()));
  }

  @Test()
  void verifyPasswordMatchesPasswordMissingRequirementsException() {
    Assertions.assertThrows(
        MissingRequirementsException.class,
        () -> customerService.verifyPasswordMatches(customer.getUsername(), null));
  }
}
