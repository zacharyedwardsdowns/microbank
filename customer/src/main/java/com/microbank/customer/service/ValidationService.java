package com.microbank.customer.service;

import com.microbank.customer.exception.ValidationException;
import com.microbank.customer.exception.validation.InvalidAddressException;
import com.microbank.customer.exception.validation.InvalidDateException;
import com.microbank.customer.exception.validation.InvalidPasswordException;
import com.microbank.customer.exception.validation.InvalidUsernameException;
import com.microbank.customer.model.Customer;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class ValidationService {

  private static final String USERNAME_REGEX = "^[A-Za-z0-9]{6,30}$";
  private static final String PASSWORD_REGEX =
      "^(?=.*[A-Za-z])((?=.*[0-9])|(?=.*[@$!%*#?]))[A-Za-z0-9@$!%*#?]{8,50}$";

  /**
   * Validate the relevant fields of a given customer and throw an exception for invalid data.
   *
   * @param customer The customer to validate.
   * @throws ValidationException Upon finding invalid data in the given customer.
   */
  public void validateNewCustomer(final Customer customer) throws ValidationException {

    if (customer.getUsername() == null || !customer.getUsername().matches(USERNAME_REGEX)) {
      throw new InvalidUsernameException(
          "Invalid username! Must be 6-30 characters long and only contain alphanumeric symbols.");
    }

    if (customer.getAddress() == null) { // Add call to USPS address verification API.
      throw new InvalidAddressException("Invalid Address! Please provide a valid US address.");
    }

    final LocalDate now = LocalDate.now();
    final LocalDate pastMaxLimit = now.minusYears(150);
    final LocalDate pastMinLimit = now.minusYears(18);
    if (customer.getDateOfBirth() == null
        || customer.getDateOfBirth().isBefore(pastMaxLimit)
        || customer.getDateOfBirth().isAfter(pastMinLimit)) {
      throw new InvalidDateException(
          "Invalid date of birth! Valid birthdays are those 18-150 years ago from today.");
    }

    if (customer.getPassword() == null || commonPassword(customer)) {
      throw new InvalidPasswordException(
          "Invalid Password! Must be between 8-50 characters long, have at least one letter and one number or symbol, and not contain common words such as 'password', or any personal information.");
    }
  }

  /**
   * Checks for a common or weak password using the PASSWORD_REGEX and given customer information.
   *
   * @param customer The customer information to check against.
   * @return True if the password is weak or common, and false otherwise.
   */
  private boolean commonPassword(final Customer customer) {
    if (!customer.getPassword().matches(PASSWORD_REGEX)) {
      return true;
    }

    final String password = customer.getPassword().toLowerCase();
    final List<String> commonPasswords = new ArrayList<>();

    commonPasswords.add("password");
    commonPasswords.add(customer.getCity());
    commonPasswords.add(customer.getAddress());
    commonPasswords.add(customer.getUsername());
    commonPasswords.add(customer.getLastName());
    commonPasswords.add(customer.getFirstName());
    commonPasswords.add(customer.getMiddleName());
    commonPasswords.add(customer.getPostalCode());
    commonPasswords.add(customer.getSocialSecurityNumber());
    commonPasswords.add(customer.getDateOfBirth().toString());

    for (final String commonPassword : commonPasswords) {
      if (commonPassword != null && password.contains(commonPassword.toLowerCase())) {
        return true;
      }
    }

    return false;
  }
}
