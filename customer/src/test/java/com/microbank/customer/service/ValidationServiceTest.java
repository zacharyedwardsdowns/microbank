package com.microbank.customer.service;

import com.microbank.customer.exception.validation.InvalidAddressException;
import com.microbank.customer.exception.validation.InvalidDateException;
import com.microbank.customer.exception.validation.InvalidPasswordException;
import com.microbank.customer.exception.validation.InvalidUsernameException;
import com.microbank.customer.model.Customer;
import com.microbank.customer.security.Sanitizer;
import com.microbank.customer.util.Util;
import java.io.File;
import java.nio.file.Files;
import java.time.LocalDate;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;

public class ValidationServiceTest {

  private static ValidationService validationService;
  private static String json;
  private Customer customer;

  @BeforeClass
  public static void setupClass() throws Exception {
    final File resource = new ClassPathResource("json/Customer.json").getFile();
    json = Files.readString(resource.toPath());
    json = Sanitizer.sanitizeJson(json);
  }

  @Before
  public void setup() throws Exception {
    validationService = new ValidationService();
    customer = Util.MAPPER.readValue(json, Customer.class);
  }

  @Test
  public void testValidateNewCustomer() throws Exception {
    validationService.validateNewCustomer(customer);
  }

  @Test(expected = InvalidUsernameException.class)
  public void testValidateNewCustomerInvalidUsernameExceptionTooShort() throws Exception {
    customer.setUsername("brian");
    validationService.validateNewCustomer(customer);
  }

  @Test(expected = InvalidUsernameException.class)
  public void testValidateNewCustomerInvalidUsernameExceptionNullUsername() throws Exception {
    customer.setUsername(null);
    validationService.validateNewCustomer(customer);
  }

  @Test(expected = InvalidUsernameException.class)
  public void testValidateNewCustomerInvalidUsernameException() throws Exception {
    customer.setUsername("zach");
    validationService.validateNewCustomer(customer);
  }

  @Test(expected = InvalidPasswordException.class)
  public void testValidateNewCustomerInvalidPasswordExceptionTooShort() throws Exception {
    customer.setPassword("nly7chr");
    validationService.validateNewCustomer(customer);
  }

  @Test(expected = InvalidPasswordException.class)
  public void testValidateNewCustomerInvalidPasswordExceptionTooLong() throws Exception {
    customer.setPassword("awholewhoppinfiftyonecharacterlongstring2testfalse!");
    validationService.validateNewCustomer(customer);
  }

  @Test(expected = InvalidPasswordException.class)
  public void testValidateNewCustomerInvalidPasswordExceptionNoNumbersOrSymbols() throws Exception {
    customer.setPassword("almostvalid");
    validationService.validateNewCustomer(customer);
  }

  @Test(expected = InvalidPasswordException.class)
  public void testValidateNewCustomerInvalidPasswordExceptionNoPersonalInfo() throws Exception {
    customer.setPassword(customer.getUsername() + "$5");
    validationService.validateNewCustomer(customer);
  }

  @Test(expected = InvalidPasswordException.class)
  public void testValidateNewCustomerInvalidPasswordExceptionNullPassword() throws Exception {
    customer.setPassword(null);
    validationService.validateNewCustomer(customer);
  }

  @Test(expected = InvalidAddressException.class)
  public void testValidateNewCustomerInvalidAddressExceptionNullAddress() throws Exception {
    customer.setAddress(null);
    validationService.validateNewCustomer(customer);
  }

  @Test(expected = InvalidDateException.class)
  public void testValidateNewCustomerInvalidDateExceptionTooYoung() throws Exception {
    final LocalDate date = LocalDate.now().minusYears(18).plusDays(1);
    customer.setDateOfBirth(date);
    validationService.validateNewCustomer(customer);
  }

  @Test(expected = InvalidDateException.class)
  public void testValidateNewCustomerInvalidDateExceptionTooOld() throws Exception {
    final LocalDate date = LocalDate.now().minusYears(150).minusDays(1);
    customer.setDateOfBirth(date);
    validationService.validateNewCustomer(customer);
  }

  @Test(expected = InvalidDateException.class)
  public void testValidateNewCustomerInvalidDateExceptionNullDateOfBirth() throws Exception {
    customer.setDateOfBirth(null);
    validationService.validateNewCustomer(customer);
  }
}
