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
import java.util.stream.Stream;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.core.io.ClassPathResource;

class ValidationServiceTest {

  private static ValidationService validationService;
  private static String json;
  private Customer customer;

  @BeforeAll
  static void setupClass() throws Exception {
    final File resource = new ClassPathResource("json/Customer.json").getFile();
    json = Files.readString(resource.toPath());
    json = Sanitizer.sanitizeJson(json);
  }

  @BeforeEach
  void setup() throws Exception {
    validationService = new ValidationService();
    customer = Util.MAPPER.readValue(json, Customer.class);
  }

  @Test
  void testValidateNewCustomer() throws Exception {
    validationService.validateNewCustomer(customer);
  }

  static Stream<String> usernameStrings() {
    return Stream.of("brian", null, "zach");
  }

  @ParameterizedTest
  @MethodSource("usernameStrings")
  void testValidateNewCustomerInvalidUsernameException(final String username) {
    customer.setUsername(username);
    Assertions.assertThrows(
        InvalidUsernameException.class, () -> validationService.validateNewCustomer(customer));
  }

  static Stream<String> passwordStrings() {
    return Stream.of(
        "nly7chr",
        "awholewhoppinfiftyonecharacterlongstring2testfalse!",
        "almostvalid",
        "zachary$5",
        null);
  }

  @ParameterizedTest
  @MethodSource("passwordStrings")
  void testValidateNewCustomerInvalidPasswordException(final String password) {
    customer.setPassword(password);
    Assertions.assertThrows(
        InvalidPasswordException.class, () -> validationService.validateNewCustomer(customer));
  }

  @Test
  void testValidateNewCustomerInvalidAddressExceptionNullAddress() {
    customer.setAddress(null);
    Assertions.assertThrows(
        InvalidAddressException.class, () -> validationService.validateNewCustomer(customer));
  }

  static Stream<LocalDate> localDateStrings() {
    return Stream.of(
        LocalDate.now().minusYears(18).plusDays(1),
        LocalDate.now().minusYears(150).minusDays(1),
        null);
  }

  @ParameterizedTest
  @MethodSource("localDateStrings")
  void testValidateNewCustomerInvalidDateException(final LocalDate date) {
    customer.setDateOfBirth(date);
    Assertions.assertThrows(
        InvalidDateException.class, () -> validationService.validateNewCustomer(customer));
  }
}
